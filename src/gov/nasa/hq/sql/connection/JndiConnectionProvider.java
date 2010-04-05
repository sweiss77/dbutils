// $Id$

package gov.nasa.hq.sql.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class obtains a java.sql.Connection object from a DataSource
 * configured using JNDI
 */
public class JndiConnectionProvider implements ConnectionProvider {

    Connection conn_ = null;
    String datasource_ = null;

    public JndiConnectionProvider() {
        // Empty constructor is needed by ConnectionProviderFactory
    }

    public JndiConnectionProvider( String dataSource ) {

        datasource_ = dataSource;
    }

    /**
     * Sets the name of the data source
     * @param properties
     * @throws NoSuchPropertyException
     */
    public void setProperties( Properties properties )
                    throws ConnectionProviderException {
        // Get the name of the data source
        datasource_ = properties.getProperty( "connection.datasource" );
    }

    /**
     * Gets a database connection
     * @return java.sql.Connection
     * @throws ConnectionProviderException
     */
    public Connection getConnection() throws ConnectionProviderException {

        try {

            DataSource ds = null;

            // Get our environment naming context and lookup the data source
            Context init_ctx = new InitialContext();
            ds = (DataSource) init_ctx.lookup( datasource_ );

            // Allocate and use a connection from the pool
            conn_ = ds.getConnection();

        } catch ( NamingException ex ) {
            //ex.printStackTrace();
            String s = "Error performing JNDI lookup: " + ex.getMessage();
            throw new ConnectionProviderException( s, ex );
        } catch ( SQLException ex ) {
            //ex.printStackTrace();
            String s = "A database error occurred: " + ex.getMessage();
            throw new ConnectionProviderException( s, ex );
        }
        return conn_;
    }

    /** Does nothing, let the connection pool handle closing connections */
    public void releaseConnection() {

        try {
            if ( conn_ != null ) {
                conn_.close();
                conn_ = null;
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }  finally {
            if (conn_ != null) {
                try { 
                    conn_.close(); 
                } catch (SQLException e) { 
                    // do nothing
                }
                conn_ = null;
            }
        }
    }

}
