// $Id$ 
package gov.nasa.hq.sql.connection;

import java.sql.Connection;

/**
 * Abstract class to encapsulate different ways of getting database connections
 * Add new concrete classes as necessary when invoking new connection getting methods and include 
 * them in the  getInstance() method. Implement the specific methods as needed.
 * @author baltner
 *
 */
public abstract class DBConnectionStrategy {
	
	static final int PROPERTIES = 1;
	static final int APP_PROPERTIES = 2;
	
	public static DBConnectionStrategy getInstance(int type)  throws DBConnectionException {
		
      //return a concrete DBConnectionStrategy object
	  DBConnectionStrategy dbConnStr = null;
	  
	  switch (type) {
	  
		  case 1: 
			  dbConnStr =  new PropertiesConnectionStr();
			  break;		  
		  case 2:
			  dbConnStr = new AppPropertiesConnectionStr();
			  break;	  
		  default: 
			  throw new DBConnectionException("You need to specify a valid connection type.");
	  }
	  
	  return dbConnStr;
	  
	}

	public abstract Connection getConnection() throws DBConnectionException ;
	public abstract Connection getConnection(String propertyFile) throws DBConnectionException ;;
	public abstract void  releaseConnection() ;
	
}
