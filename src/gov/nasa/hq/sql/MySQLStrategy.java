// $Id$ 

package gov.nasa.hq.sql;

import gov.nasa.hq.util.StringUtil;

import java.text.SimpleDateFormat;

public class MySQLStrategy extends DBStrategy {

    /**
     * Implements MySQL's regular expression for word boundaries
     * 
     * @param fieldname
     * @param value
     * @param caseSensitive
     * @return java.lang.String
     */
    @Override
    public String getRegexpWB(String fieldname, Object value, boolean caseSensitive) {

        // MySQL regular expression for word boundary
        StringBuffer buf = new StringBuffer();
        buf.append(fieldname + " RLIKE ");
        buf.append("'[[:<:]]");
        buf.append(value.toString());
        buf.append("[[:>:]]'");
        return new String(buf);

    }

    /**
     * Returns a java.text.SimpleDateFormat object which can be used to compare
     * dates in a MySQL database
     * 
     * @return SimpleDateFormat
     */
    @Override
    public SimpleDateFormat getSimpleDateFormat() {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public SimpleDateFormat getSimpleDateTimeFormat() {
        return getSimpleDateFormat();
    }

    @Override
    public String replace(String str) {
        if (str.contains("'"))
            str = StringUtil.replace(str, "'", "\\'");

        return str;
    }
}
