//$Id: $
package gov.nasa.hq.sql;

import java.text.DateFormat;
import java.util.Date;

public class MySQLStrategy extends DBStrategy {


	public String getRegexpWB(String fieldname, Object value, boolean caseSensitive ) {
	  //implements MySQL's regular expression for word boundaries

        //MySQL regular expression for word boundary
		StringBuffer buf = new StringBuffer();
		buf.append(fieldname + " RLIKE ");
    	buf.append("'[[:<:]]");
    	buf.append(value.toString() );
    	buf.append("[[:>:]]'");                 
    	return new String( buf );    
		
	}
	
	public DateFormat formatDate(Date date) {
		//TBD
		return null;
	}
	
}