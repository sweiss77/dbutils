// $Id$ 

package gov.nasa.hq.sql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OracleStrategy extends DBStrategy {

    /**
     * Implements Oracle's regular expression for word boundaries
     * @param fieldname
     * @param value
     * @param caseSensitive
     * @return java.lang.String
     */
    public String getRegexpWB( String fieldname,
                               Object value,
                               boolean caseSensitive ) {

        //Oracle regular expression for word boundary   	
        StringBuffer buf = new StringBuffer();
        buf.append( "REGEXP_LIKE(" );
        buf.append( fieldname );
        buf.append( ", '(^" );
        buf.append( value.toString() );
        buf.append( "[[:blank:]|[:punct:]])|([[:blank:]|[:punct:]]" );
        buf.append( value.toString() );
        buf.append( "$)|([^[:alnum:]]" );
        buf.append( value.toString() );
        buf.append( "[^[:alnum:]])'" );
        if ( !caseSensitive )
            buf.append( ",'i'" );
        else
            buf.append( ",'c'" );
        buf.append( ")" );

        return new String( buf );

    }

    /**
     * Returns a java.text.SimpleDateFormat object which can be used to
     * compare dates in a Oracle database
     * @return SimpleDateFormat
     */
    public SimpleDateFormat getSimpleDateFormat() {

        return new SimpleDateFormat( "dd-MMM-yy" );
    }

    public boolean requireAllSelectColumnsInGroupBy() {

        return true;
    }

}
