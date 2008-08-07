// $Id$

package gov.nasa.hq.sql.connection;

import gov.nasa.hq.properties.*;

/**
 * Factory class which creates concrete classes that implement the
 * {@link ConnectionProvider} interface
 */
public class ConnectionProviderFactory {

    /**
     * Creates a default {@link ConnectionProvider}
     * @return {@link ConnectionProvider}
     * @throws {@link ConnectionProviderException}
     */
    public static ConnectionProvider getConnectionProvider()
        throws ConnectionProviderException {

        return getConnectionProvider( "default" );
    }

    /**
     * Creates a {@link ConnectionProvider} specified by the "name" argument.
     * This argument should be the name of a PropertyGroup which contains
     * whatever specific configuration information is needed to get a
     * database connection (java.sql.Connection) from the specific framework
     * (if any) being used.
     * @return {@link ConnectionProvider}
     * @throws {@link ConnectionProviderException}
     */
    public static ConnectionProvider getConnectionProvider( String name )
        throws ConnectionProviderException {

        ConnectionProvider provider = null;

        try {

            ApplicationProperties props = ApplicationProperties.getInstance();
            PropertySet dbprops = props.getPropertySet( "database" );
            PropertyGroup prov_props = dbprops.getPropertyGroup( name );
            String class_name = prov_props.getPropertyValue( "provider" );
            Class<?> c = Class.forName( class_name );
            Object obj = c.newInstance();
            provider = (ConnectionProvider) obj;
            provider.setProperties( prov_props );

        } catch ( PropertiesException ex ) {
            String err = "Unable to get configuration info from properties!";
            throw new ConnectionProviderException( err, ex );
        } catch ( ClassNotFoundException ex ) {
            String err = "ConnectionProvider class does not exist!";
            throw new ConnectionProviderException( err, ex );
        } catch ( ClassCastException ex ) {
            String err = "Unable to cast to correct ConnectionProvider class!";
            throw new ConnectionProviderException( err, ex );
        } catch ( InstantiationException ex ) {
            String err = "Unable to instantiate ConnectionProvider class!";
            throw new ConnectionProviderException( err, ex );
        } catch ( IllegalAccessException ex ) {
            throw new ConnectionProviderException( ex.getMessage(), ex );
        }

        return provider;
    }
}
