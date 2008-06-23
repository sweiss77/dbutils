
package gov.nasa.hq.sql;

import java.sql.*;
import java.util.*;

/**
 * This is a proxy class which provides an interface to a
 * {@link java.sql.Connection} object. The {@link DBConnectionManager} class
 * manages a pool of these objects
 * @version $Revision$
 * @author <a href="mailto:sweiss@hq.nasa.gov">Steve Weiss</a>
 */
public class DBConnection {

    /** The java.sql.Connection object */
    private Connection _conn = null;
    /** The system time at which the connection expires */
    private long _expireTimeout;
    /** Connection expires at this time if it is idle */
    private long _idleTimeout;
    /** Flag which indicates the connection has experienced an error */
    private boolean _tainted = false;
    //private boolean _initialized = false;

    /**
     *  Expiration time for connection, in millseconds.
     *  This limit will be set when the connection is created,
     *  and when it is reached, the connection will be destroyed.
     */
    public long _expireLimit = -1;

    /**
     *  Maximum idle time for connection, in millseconds.  The
     *  connection will be destroyed when this limit is reached.
     */
    public long _idleLimit = -1;

    /**
     *  Create an open connection to specified database.
     */

    public DBConnection( String url, String _username,
    		   			 String _password, long idleLimit,
    //long expireLimit, LogManager logmgr ) {
                         long expireLimit ) {
    	
        String username = _username;
        String password = _password;

        try {

            _conn = java.sql.DriverManager.getConnection( url,
                                                          username,
                                                          password );

            _idleLimit = idleLimit;
            _expireLimit = expireLimit;
            refreshIdleTimeout();

            if ( expireLimit > 0 )
                _expireTimeout = System.currentTimeMillis() + expireLimit;

        } catch ( Exception e ) {
            //syslog( "Error create DBConnection: " + e.getMessage() );
            e.printStackTrace();
        }

    } // END DBConnection()

    public Connection getConnection() {
        return _conn;
    }

    /**
     *  Checks to see if this connection has expired
     *  @return <code>true</code> if the connection has expired.
     */
    protected boolean expired() {

        long currtime = System.currentTimeMillis();
        return ( _expireTimeout > 0 && currtime >= _expireTimeout )
            || ( _idleTimeout > 0 && currtime >= _idleTimeout )
            || ( _tainted );
    }

    /**
     *  Make the connection fresh again - this should ideally have 
     *  some SQL to free stuff up on the DB end.
     */
    protected void refreshIdleTimeout() {

        _idleTimeout = System.currentTimeMillis() + _idleLimit;
    }

    /**  Call to signify that this connection has had an exception. */
    public void taint() {

        //syslog( "Tainted Connection" );
        _tainted = true;
    }

    /**
     *  This connection will be marked as tainted if it has an exception.
     *  It should be destroyed by connection pooler.
     * @return <code>boolean</code>
     */
    protected boolean tainted() {

        return _tainted;
    }

    public Statement createStatement() {

        Statement s = null;
        if ( _conn != null ) {
            try {
                s = _conn.createStatement();
            } catch ( SQLException ex ) {
                //
            }
        }
        return s;
    }

    /** 
     *  Prepare an SQL statement and return a PreparedStatement. 
     *  Note: it is the user's responsibility to call close() on
     *  the statement when no longer used.
     *  @param sql the SQL statement
     *  @return a PreparedStatement that has to have its '?' elements bound
     */
    public PreparedStatement prepareStatement( String sql ) {

        try {
            return _conn.prepareStatement( sql );
        } catch ( Exception e ) {
            //syslog( "DBConnection.prepareStatement: "+e );
            taint();
            return null;
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {

        return _conn.getMetaData();
    }

    public ResultSet doSelect( SQLQuery query ) throws SQLException {

        String sql = query.getSQL();
        return doSelect( sql );
    }

    /** 
     * Perform SQL statement and return ResultSet. Calls its
     * DBConnectionManager's syslog() method if the query fails, and prints
     * a stack trace as well. We tried testing Connection.isClosed() to
     * allow reconnection, but it's generally a bogus function.
     *
     * @param <code>sql</code> the SQL statement
     * @return <code>ResultSet</code>, or null if the sql caused an exception.   
     */
    public ResultSet doSelect( String sql ) throws SQLException {

        try {
            Statement stmt = _conn.createStatement();
            ResultSet rs = stmt.executeQuery( sql );
            return rs;
        } catch ( SQLException e ) {
            //syslog( "Database error, " + e.getMessage() );
            taint();
            throw e;
        } catch ( java.lang.OutOfMemoryError oom ) {
            throw new SQLException( oom.toString() );
        }
    }

    /**
     * Executes a SQL update
     * @param <code>sql</code>
     * @return <code>int</code> The number of records updated
     */
    public int doUpdate( String sql ) throws SQLException {

        int count = 0;
        try {
            Statement stmt = _conn.createStatement();
            count = stmt.executeUpdate( sql );
            stmt.close();
        } catch ( SQLException ex ) {
            taint();
            ex.printStackTrace();
            throw ex;
        }
        return count;
    }

    public void setReadOnly( boolean b ) {

        try {
            _conn.setReadOnly( b );
        } catch ( SQLException e ) {
            //syslog( "Database error: " + e.getMessage() );
            e.printStackTrace();
        }
    }

    public boolean doQuery( String sql ) throws SQLException {

        try {
            Statement stmt = _conn.createStatement();
            return stmt.execute( sql );
        } catch ( SQLException e ) {
            taint();
            throw e;
        }
    }

    public void close() {

        try {
            if ( _conn != null )
                _conn.close();
        } catch ( Exception e ) {
            // Fine - the garbage collector can do it then!
            ;
        }
    }

}
