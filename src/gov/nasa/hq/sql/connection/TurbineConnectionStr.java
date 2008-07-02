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
public class TurbineConnectionStr extends DBConnectionStrategy{

	DBConnection dbconn = null;
	 
	public Connection getConnection()  throws DBConnectionException {
		 	
		 	Connection conn = null;
	        try {
		 	       dbconn = TurbineDB.getConnection();
		 	       conn = dbconn.getConnection();
		 	} catch ( SQLException ex ) {
		 	               
		 	} catch (Exception ex ) {
		 	               
		 	}
		 	  return conn;
	}

    public void releaseConnection() {

        try {
            if ( dbconn != null ) {
                TurbineDB.releaseConnection( dbconn );
            }
        } catch ( Exception e ) {
            if ( e.getCause() == null || !( e instanceof RuntimeException ) ) {
                throw new RuntimeException( e );
            } else {
                throw (RuntimeException) e;
            }
        }
    }
    
	public Connection getConnection(String propertyFile) throws DBConnectionException {
		//not used in this case
		String msg = null;
		try 
		{
			msg = "Use the no-argument version of getConnection() to get a Connetion from ";			
		}
		catch (Exception e) {
	        throw new DBConnectionException(msg + this.getClass().getName() );	
		}

		return null;
	}
	
}