// $Id$ 

package gov.nasa.hq.sql;

import java.text.SimpleDateFormat;
import gov.nasa.hq.util.StringUtil;

/**
 * Abstract class to encapsulate database-specific behavior in this library. 
 * Add new concrete classes as necessary when using new databases and include 
 * them in the  getInstance() method. Implement the specific methods as needed.
 * @author baltner
 *
 */
public abstract class DBStrategy {

    public static DBStrategy getInstance( String dbProductName )
        throws DBStrategyException {

        //return a concrete DBStrategy object
        if ( dbProductName.toUpperCase().equals( "MYSQL" ) ) {
            return new MySQLStrategy();
        } else if ( dbProductName.toUpperCase().equals( "ORACLE" ) ) {
            return new OracleStrategy();
        } else {
            throw new DBStrategyException( "Unsupported database vendor!" );
        }
    }

    /**
     * By default returns false, but child classes should over-ride and
     * return true if the database requires all columns from the
     * SELECT CLAUSE to be in the GROUP BY clause
     * 
     * @return boolean
     */
    public boolean requireAllSelectColumnsInGroupBy() {

        return false;
    }

    public abstract String getRegexpWB( String fieldname,
                                        Object value,
                                        boolean caseSensitive );

    public abstract SimpleDateFormat getSimpleDateFormat();
    public abstract SimpleDateFormat getSimpleDateTimeFormat();
    public abstract String replace( String str );
}
