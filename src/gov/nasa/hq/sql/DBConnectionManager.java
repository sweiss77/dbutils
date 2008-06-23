// $Id$

package gov.nasa.hq.sql;

import java.sql.*;
import java.lang.*;
import java.util.*;

import gov.nasa.hq.properties.*;

/**
 * A singleton class which manages a pool of database connections.
 * @version $Revision$
 * @author <a href="mailto:sweiss@hq.nasa.gov">Steve Weiss</a>
 */
public class DBConnectionManager extends Thread {

    private static DBConnectionManager _connMgr;
    String _myName = null;
    int _checkoutCount = 0;
    String _dbURL;
    String _driverName;
    Properties _props;
    private Stack _pool = new Stack();
    private Stack _oldpool = new Stack();
    String _username;
    String _password;
    boolean _mgrFinished;
    boolean _threadFinished;

    // The following are default values which can be over-ridden

    /**
     *  Connection pool will be scanned every specified milliseconds.
     *  Can be altered using properties file passed to constructor. 
     *  Properties file should specify seconds, not milliseconds.
     */
    long DEFAULT_SLEEP_TIME = 90 * 1000; // 90 seconds

    /**
     *  Minimum number of connections to keep in the connection pool.
     *  Connections will always be given when requested; the connection
     *  pool simply maintains a ready-made set of connections to minimize 
     *  the delay of making one from scratch.
     */
    int DEFAULT_MIN_POOL_SIZE = 6;

    /** 
     *  Can be set using the property DBConn_expire_limit, which should be
     *  specified in seconds.  See DBConnection.expire_limit.
     */
    long DEFAULT_EXPIRE_LIMIT = 60 * 60 * 1000;

    /** 
     *  Can be set using the property DBConn_idle_limit, which should be
     *  specified in seconds.  See DBConnection.idle_limit.
     */
    long DEFAULT_IDLE_LIMIT = 10 * 60 * 1000;

    long _sleepTime;
    int _minPoolSize;
    long _idleLimit;
    long _expireLimit;
    
    /**
     * Constructor which takes as arguments a <code>Properties</code> object
     * as its only constructor. This contains the information needed
     * to initialize the database connection. This information includes the
     * URL to the database, the username and password to use for connecting
     * to the database and the name of the JDBC driver
     * @param <code>lmgr</code> the application's <code>LogManager</code>
     * @param <code>p</code> the <code>Properties</object>
     */
    private DBConnectionManager() throws Exception {

        // Get stuff we need.
        _myName = this.getClass().getName();
        _mgrFinished = false;

        ApplicationProperties props = ApplicationProperties.getInstance();
        
        try {

            PropertyGroup dbprops = props.getPropertyGroup( "system",
                                                            "database" );
            
            _dbURL = dbprops.getPropertyValue( "url" );
            _username = dbprops.getPropertyValue( "username" );
            _password = dbprops.getPropertyValue( "password" );
            _driverName = dbprops.getPropertyValue( "jdbcDriver" );
            Class.forName( _driverName );

            // begin add to properties attribute
            _props = new Properties( );
            _props.setProperty( "DB_USERNAME", _username );
            _props.setProperty( "DB_PASSWORD", _password );
            // end add to properties attribute

            //String temp = p.getProperty( name,
                                         //String.valueOf( defaultval / 1000 ) );
            //long result = Long.parseLong( temp ) * 1000;

            try {
                _sleepTime = dbprops.getPropertyValueInt( "threadSleepTime" );
            } catch ( Exception ex ) {
                _sleepTime = DEFAULT_SLEEP_TIME;
            }

            //_sleepTime = getMillisecondsProperty( "DBCONNMANAGER_SLEEP_TIME",
                                                  //dbprops,
                                                  //_sleepTime );

            //_minPoolSize = Integer.parseInt( p.getProperty( "DBCONNMANAGER_MIN_POOL_SIZE",
                                                            //String.valueOf( _minPoolSize ) ) );
            try {
                _minPoolSize = dbprops.getPropertyValueInt( "minPoolSize" );
            } catch ( Exception ex ) {
                _minPoolSize = DEFAULT_MIN_POOL_SIZE;
            }

            //_idleLimit = this.getMillisecondsProperty( "DBCONN_IDLE_LIMIT",
                                                       //dbprops,
                                                       //_idleLimit );
            try {
                _idleLimit = dbprops.getPropertyValueInt( "connectionIdleLimit" );
            } catch ( Exception ex ) {
                _idleLimit = DEFAULT_IDLE_LIMIT;
            }

            //_expireLimit = this.getMillisecondsProperty( "DBCONN_EXPIRE_LIMIT",
                                                         //dbprops,
                                                         //_expireLimit );
            try {
                _expireLimit = dbprops.getPropertyValueInt( "connectionExpireLimit" );
            } catch ( Exception ex ) {
                _expireLimit = DEFAULT_EXPIRE_LIMIT;
            }

        } catch ( PropertiesException ex ) {
        	ex.printStackTrace( );
        }

        //        long idle_limit = getMillisecondsProperty( "DBCONN_IDLE_LIMIT",
        //                                                   _props,
        //                                                   10000 );
        //
        //        long expire_limit = getMillisecondsProperty( "DBCONN_EXPIRE_LIMIT",
        //                                                     _props,
        //                                                     10000 );

        // Make my manager thread happen.
        this.start();
    }

    /**
     * Returns a static instance of the DBConnectionManager
     * @param <code>lmgr</code> the application <code>LogManager</code>
     * @param <code>props</code> a <code>Properties</code> object containing
     *        configuration information
     * @return <code>DBConnectionManager</code>
     * @throws <code>Exception</code> if an initialization error occors
     */
    public static DBConnectionManager getInstance() {

        if ( _connMgr == null ) {
        	try {
            _connMgr = new DBConnectionManager();
            } catch ( Exception ex ) {
                ex.printStackTrace( );
            }
        }
        return _connMgr;
    }

    /**
     * Sets the name of the JDBC driver
     * @param <code>drivername</code> a <code>String</code> containing the name
     *        of the driver
     */
    void setDriverName( String drivername ) {

        _driverName = drivername;
    }

    /**
     * Retrieves the name of the JDBC driver
     * @return <code>String</code>
     */
    public String getDriverName() {

        return _driverName;
    }

    //    protected long getMillisecondsProperty( String name, PropertyGroup pg,
    //                                            long defaultval ) {
    //
    //        String temp = pg.getPropertyValue( name, String.valueOf( defaultval / 1000 ) );
    //        long result = Long.parseLong( temp ) * 1000;
    //        return result;
    //    }

    protected String poolStatus() {

        return "\n  connection count: in pool=" + _pool.size()
            + " checked out=" + _checkoutCount;
    }

    public Thread getThread() {

        return Thread.currentThread();
    }

    /**
     * Monitors the pool of <code>DBConnection</code> objects.
     * Checks for expired
     * connections and removes them, and adds new connections to the pool if
     * necessary.
     */
    public void run() {

        String ID = "run() ";
        _threadFinished = false;

        //while(true) {
        while ( !_mgrFinished ) {

            // Put connections in temp pool, and be quick about it:
            synchronized ( _pool ) {
                while ( !_pool.empty() )
                    _oldpool.push( _pool.pop() );
            }

            // Now in a relaxed manner, scan the connections for expiration,
            // and put them back if they're good:
            while ( !_oldpool.empty() ) {

                DBConnection c = (DBConnection) _oldpool.pop();

                if ( c.expired() ) {
                    //debug( ID + "Database connection has expired: "+ c );
                    c.close();
                    c = null;
                } else {
                    _pool.push( c );
                }
            }

            // Add any missing connections to pool
            while ( _pool.size() < _minPoolSize ) {
                _pool.push( createDBConnection() );
            }

            // Clean up our stuff and garbage collect.  
            // Have to be clean since this will run forever.
            System.gc();

            // Go to sleep for a while.
            try {
                //debug( ID + "DBConnectionManager going to sleep", 5 );
                this.sleep( _sleepTime );
                //debug( "DBConnectionManager waking up", 5 );
            } catch ( InterruptedException e ) {
                //debug( "DBConnectionManager got woken up", 5 );
                ; // Someone woke me up!  Guess we better go to work.
            }

        } // END while ( !_mgrFinished )

        _threadFinished = true;
        debug( ID + "Stopped" );

    } // END run()

    public void force_checkpoint() {

        this.interrupt();
        //syslog( "Checkpoint Forced", 7 );
    }

    /**
     * Gets a DBConnection object from the connection pool
     * @return <code>DBConnection</code>
     */
    public DBConnection checkOut() {

        return checkout();
    }

    /**
     * Gets a DBConnection object from the connection pool
     * @return <code>DBConnection</code>
     */
    public DBConnection checkout() {

        return getConnection();
    }

    /**
     * Gets a DBConnection object from the connection pool
     * @return <code>DBConnection</code>
     */
    public DBConnection getConnection() {

        String ID = "getConnection() ";
        DBConnection returnval = null;
        String dbg = "";

        synchronized ( _pool ) {

            if ( !_pool.empty() ) {
                dbg += " obtained from pool; ";
                returnval = (DBConnection) _pool.pop();
            }
        }

        if ( returnval == null ) {
            dbg += " created; ";
            returnval = createDBConnection();
        }

        _checkoutCount++;

        if ( _checkoutCount > 150 )
            dbg += " WARNING!!!! ";
        //syslog("checkout():\n  " + returnval + dbg + poolStatus(), 8 );

        //debug( ID + "Pool size: " + _pool.size() );
        return returnval;
    }

    /**
     * Returns a {@link DBConnection} to the connection pool
     * @param <code>conn</code>
     */
    public void checkIn( DBConnection conn ) {

        checkin( conn );
    }

    /**
     * Returns a {@link DBConnection} to the connection pool
     * @param <code>conn</code>
     */
    public void checkin( DBConnection conn ) {

        releaseConnection( conn );
    }

    public void releaseConnection( DBConnection conn ) {

        String ID = "releaseConnection() ";
        String dbg = "" + conn;

        if ( conn.expired() ) {
            conn.close();
            conn = null;
            dbg += " killed;";
        } else {
            conn.refreshIdleTimeout();
            conn.setReadOnly( false );
            _pool.push( conn );
            dbg += " returned to pool;";
        }

        //debug( ID + "Pool size: " + _pool.size() );
        _checkoutCount--;
        //         debug("DBConnectionManager.checkIn(): \n  " + dbg +
        //               poolStatus(), debugLevel);
    }

    /**
     * Stops the connection manager by calling its {@link destroy()} method
     */
    public void stopManager() {

        String ID = "stopManager() ";
        boolean done = false;
        // Close all db connections
        closeall();
        while ( !done ) {
            try {
                finalize();
                done = true;
            } catch ( Throwable t ) {
                t.printStackTrace();
                done = false;
            }
        }
    }

    /** Closes all connections in the pool */
    public void closeall() {

        while ( !_pool.empty() ) {
            DBConnection conn = (DBConnection) _pool.pop();
            conn.close();
        }
    }

    /**
     * Cleans up, by calling <code>closeAll</code> but waits for executing
     * threads to finish
     */
    protected void finalize() throws Throwable {

        String ID = "finalize() ";
        _mgrFinished = true;

        while ( !_threadFinished ) {
            try {
                this.join();
            } catch ( InterruptedException i ) {
            }
        }
        this.closeall();
        debug( ID + "Goodbye" );
    }

    /**
     * Creates a new <code>DBConnection</code> object
     * @return <code>DBConnection</code> the new connection
     */
    private DBConnection createDBConnection() {

//        long idle_limit = getMillisecondsProperty( "DBCONN_IDLE_LIMIT",
//                                                   _props,
//                                                   10000 );
//
//        long expire_limit = getMillisecondsProperty( "DBCONN_EXPIRE_LIMIT",
//                                                     _props,
//                                                     10000 );

    	  return createDBConnection( _dbURL,
        						   _username,
        						   _password,
                                   _idleLimit,
                                   _expireLimit );
    }

    /**
     * Creates a new <code>DBConnection</code> object with the specified
     * properties.
     * @param <code>url</code> the database URL string
     * @param <code>config</code> a <code>Properties</code> object containing
     *        configuration information
     * @param <code>idle_limit</code> maximum idle time for the connnection
     * @param <code>expireLimit</code> maximum time-to-live for the connnection
     * @return <code>DBConnection</code> the new connection
     */
    protected DBConnection createDBConnection( String url, 
    		                                   String username,
			   								   String password,
                                               long idle_limit,
                                               long expireLimit ) {

        return new DBConnection( _dbURL, username, password, idle_limit, expireLimit );
    }

    //    public static DBConnectionManager getDBConnectionManager( Properties props )
    //        throws Exception {
    //
    //        return getInstance( props );
    //    }

    //    public static DBConnectionManager getInstance( Properties props ) {
    //
    //        if ( _connMgr == null ) {
    //            _connMgr = new DBConnectionManager( props );
    //        }
    //        return _connMgr;
    //    }
    //
    private void debug( String message ) {

        System.out.println( _myName + ": " + message );
    }
}
