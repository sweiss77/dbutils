// $Id$ 

package gov.nasa.hq.sql;

import gov.nasa.hq.util.StringUtil;

import java.text.SimpleDateFormat;

public class OracleStrategy extends DBStrategy {

    /**
     * Implements Oracle's regular expression for word boundaries
     * 
     * @param fieldname
     * @param value
     * @param caseSensitive
     * @return java.lang.String
     */
    @Override
    public String getRegexpWB(String fieldname, Object value, boolean caseSensitive) {

        // Oracle regular expression for word boundary
        StringBuffer buf = new StringBuffer();
        buf.append("REGEXP_LIKE(");
        buf.append(fieldname);
        buf.append(", '(^");
        buf.append(value.toString());
        buf.append("$)|");
        buf.append("(^");
        buf.append(value.toString());
        buf.append("[[:blank:]|[:punct:]])|([[:blank:]|[:punct:]]");
        buf.append(value.toString());
        buf.append("$)|([^[:alnum:]]");
        buf.append(value.toString());
        buf.append("[^[:alnum:]])'");
        if (!caseSensitive)
            buf.append(",'i'");
        else
            buf.append(",'c'");
        buf.append(")");

        return new String(buf);

    }

    /**
     * Returns a java.text.SimpleDateFormat object which can be used to compare
     * dates in a Oracle database
     * 
     * @return SimpleDateFormat
     */
    @Override
    public SimpleDateFormat getSimpleDateFormat() {

        return new SimpleDateFormat("dd-MMM-yy");
    }

    @Override
    public SimpleDateFormat getSimpleDateTimeFormat() {
        return new SimpleDateFormat("dd-MM-yy HH:mm:ss.S");
    }

    @Override
    public boolean requireAllSelectColumnsInGroupBy() {

        return true;
    }

    @Override
    public String replace(String str) {
        if (str.contains("'"))
            str = StringUtil.replace(str, "'", "\\''");

        return str;
    }

}
