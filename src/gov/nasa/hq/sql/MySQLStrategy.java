// $Id$ 

package gov.nasa.hq.sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySQLStrategy extends DBStrategy {

    /**
     * Implements MySQL's regular expression for word boundaries
     * @param fieldname
     * @param value
     * @param caseSensitive
     * @return java.lang.String
     */
    public String getRegexpWB( String fieldname,
                               Object value,
                               boolean caseSensitive ) {

        //MySQL regular expression for word boundary
        StringBuffer buf = new StringBuffer();
        buf.append( fieldname + " RLIKE " );
        buf.append( "'[[:<:]]" );
        buf.append( value.toString() );
        buf.append( "[[:>:]]'" );
        return new String( buf );

    }

    /**
     * Returns a java.text.SimpleDateFormat object which can be used to
     * compare dates in a MySQL database
     * @return SimpleDateFormat
     */
    public SimpleDateFormat getSimpleDateFormat() {

        return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    }

    public SimpleDateFormat getSimpleDateTimeFormat() {
        return getSimpleDateFormat();
    }

}
