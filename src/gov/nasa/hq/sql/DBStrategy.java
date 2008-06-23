// $Id: $
package gov.nasa.hq.sql;

import java.text.DateFormat;
import java.util.Date;

/**
 * Interface to be implemented by concrete strategy classes to encapsulate database-specific behavior
 * in this library. Add new concrete classes as necessary when using new databases and implement
 * the specific methods needed.
 * 
 * @author baltner
 *
 */
public interface DBStrategy {

	public String getRegexp(String fieldname, Object value, boolean caseSensitive ) ;
	
	public DateFormat formatDate(Date date) ;
	
}
