//$Id$ 

package gov.nasa.hq.sql.connection;


import java.sql.Connection;
import java.sql.SQLException;
import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.db.pool.DBConnection;
import org.apache.turbine.util.Log;

/**
 * Concrete Connection strategy that uses Turbine to establish and close Connections
 */
public class PropertiesConnectionStr extends DBConnectionStrategy{


    Connection conn_ = null;

    public Connection getConnection(String propertyFile) throws DBConnectionException {

       /*
 		String driver = null;
        String url = null;
        String username = null;
        String password = null;

        ApplicationProperties props = ApplicationProperties.getInstance();

        try {
            PropertyGroup conn =
                props.getPropertyGroup( "database", "connection" );
            url = conn.getPropertyValue( "url" );
            driver = conn.getPropertyValue( "driverName" );
            username = conn.getPropertyValue( "username" );
            password = conn.getPropertyValue( "password" );
            Class.forName( driver );
            conn_ = DriverManager.getConnection( url, username, password );
        } catch ( PropertiesException ex ) {
            ex.printStackTrace();
            String ss = "Unable to get database connection properties: ";
            throw new DBConnectionException( ss + ex.getMessage(), ex );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            throw new DBConnectionException( "Unable to open database connection: "
                + ex.getMessage(), ex );
        }
        */

        return conn_;
    }

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
    
	public Connection getConnection() throws DBConnectionException{
		//not used in this case
		String msg = null;
		try 
		{
			msg = "Use the getConnection(String propertyFile) method to get a Connetion from ";
		}
		catch (Exception e) {
	        throw new DBConnectionException(msg + this.getClass().getName() );	
		}
	
		return null;
	}
	
}