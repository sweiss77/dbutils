// $Id$

package gov.nasa.hq.sql.connection;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import gov.nasa.hq.properties.NoSuchPropertyException;
import gov.nasa.hq.properties.PropertyGroup;

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
    public void setProperties( PropertyGroup properties )
        throws NoSuchPropertyException {

        // Get the name of the data source
        datasource_ = properties.getPropertyValue( "datasource" );
    }

    /**
     * Gets a database connection
     * @return java.sql.Connection
     * @throws ConnectionProviderException
     */
    public Connection getConnection() throws ConnectionProviderException {

        try {

            DataSource ds = null;

            // TODO - Need to add code to work with OC4J
            // Get our environment naming context
            Context init_ctx = new InitialContext();

            if ( !datasource_.startsWith( "java:comp/env" ) ) {
                Context env_ctx = (Context) init_ctx.lookup( "java:comp/env" );
                ds = (DataSource) env_ctx.lookup( datasource_ );
            } else {
                // Look up our data source
                ds = (DataSource) init_ctx.lookup( datasource_ );
            }

            // Allocate and use a connection from the pool
            conn_ = ds.getConnection();

        } catch ( NamingException ex ) {
            ex.printStackTrace();
            throw new ConnectionProviderException( ex );
        } catch ( SQLException ex ) {
            ex.printStackTrace();
            throw new ConnectionProviderException( "Database error!", ex );
        }
        return conn_;
    }

    /** Does nothing, let the connection pool handle closing connections */
    public void releaseConnection() {

    }

}
