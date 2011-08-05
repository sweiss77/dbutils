// $Id$
package gov.nasa.hq.sql.connection;

import java.sql.Connection;
import java.util.Properties;

//import gov.nasa.hq.properties.NoSuchPropertyException;
//import gov.nasa.hq.properties.PropertyGroup;

/**
 * Interface for concrete classes which get database connections
 * (java.sql.Connection) from various sources.
 */
public interface ConnectionProvider {

    /**
     * Gets a database connection
     * 
     * @return java.sql.Connection
     * @throws {@link ConnectionProviderException}
     */
    public abstract Connection getConnection() throws ConnectionProviderException;

    /**
     * Stores the information that a child class needs to get a database
     * connection
     * 
     * @param properties
     * @throws {@link NoSuchPropertyException}
     */
    public abstract void setProperties(Properties properties)
        throws ConnectionProviderException;

    /**
     * Releases the database connection
     */
    public abstract void releaseConnection();
}
