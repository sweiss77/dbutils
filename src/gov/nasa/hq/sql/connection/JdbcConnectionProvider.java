// $Id$

package gov.nasa.hq.sql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import gov.nasa.hq.properties.ApplicationProperties;
import gov.nasa.hq.properties.NoSuchPropertyException;
import gov.nasa.hq.properties.PropertyGroup;

/**
 * This class provides a java.sql.Connection by calling
 * DriverManager.getConnection(url,username, password). It reads
 * the connection information from {@link ApplicationProperties}
 */
public class JdbcConnectionProvider implements ConnectionProvider {

    Connection conn_ = null;
    String driver_ = null;
    String url_ = null;
    String username_ = null;
    String password_ = null;

    /**
     * Sets the configuration information needed to obtain a database connection
     * The {@link PropertyGroup} argument should contain properties for
     * "driverName", "url", "username", and "password".
     * @param properties
     * @throws {@link NoSuchPropertyException}
     */
    public void setProperties( PropertyGroup properties ) throws NoSuchPropertyException {

        driver_ = properties.getPropertyValue( "driverName" );
        url_ = properties.getPropertyValue( "url" );
        username_ = properties.getPropertyValue( "username" );
        password_ = properties.getPropertyValue( "password" );
    }

    /**
     * Creates a single database connection
     * @return Connection
     * @throws ConnectionProviderException
     */
    public Connection getConnection() throws ConnectionProviderException {

        try {
            Class.forName( driver_ );
            conn_ = DriverManager.getConnection( url_, username_, password_ );
        } catch ( ClassNotFoundException ex ) {
            String err = "Unable to find database driver class";
            throw new ConnectionProviderException( err, ex );
        } catch ( SQLException ex ) {
            throw new ConnectionProviderException( ex.getMessage(), ex );
        }

        return conn_;

    }

    /** Closes the connection */
    public void releaseConnection() {

        try {
            if ( conn_ != null ) {
                conn_.close();
                conn_ = null;
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }

    }

}
