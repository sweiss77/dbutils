// $Id: $
package gov.nasa.hq.sql;

import java.text.DateFormat;
import java.util.Date;

/**
 * Abstract class to encapsulate database-specific behavior in this library. 
 * Add new concrete classes as necessary when using new databases and include 
 * them in the  getInstance() method. Implement the specific methods as needed.
 * @author baltner
 *
 */
public abstract class DBStrategy {
	
	public static DBStrategy getInstance(String dbProductName) {
		
      //return a concrete DBStrategy object, using OracleStrategy as the default
	  if (dbProductName.equals("MySQL")) return new MySQLStrategy();  
	    else return new OracleStrategy();	
	}

	public abstract String getRegexpWB(String fieldname, Object value, boolean caseSensitive ) ;
	
	public abstract DateFormat formatDate(Date date) ;
	
}
