// $Id$
package gov.nasa.hq.sql;

import java.text.DateFormat;
import java.util.Date;

public class OracleStrategy extends DBStrategy {


	public String getRegexpWB(String fieldname, Object value, boolean caseSensitive ) {
		
	  //implements Oracle's Regexp_Like regular expression for word boundaries
	  
      //Oracle regular expression for word boundary   	
	  StringBuffer buf = new StringBuffer();
	  buf.append( "REGEXP_LIKE(" );
  	  buf.append( fieldname );
  	  buf.append(", '(^");
  	  buf.append(value.toString() );
  	  buf.append("[[:blank:]|[:punct:]])|([[:blank:]|[:punct:]]");
  	  buf.append(value.toString() );
  	  buf.append("$)|([^[:alnum:]]");
  	  buf.append(value.toString() );
  	  buf.append("[^[:alnum:]])'");
  	  if ( !caseSensitive ) buf.append(",'i'");
  	    else buf.append(",'c'"); 
  	  buf.append(")");
  	  
	  return new String( buf ); 
		
	}
	
	public DateFormat formatDate(Date date) {
      //TBD
	  return null;
	}
	
}