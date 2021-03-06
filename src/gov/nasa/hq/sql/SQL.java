// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL is an abstract base class used to create SQL clauses which can be
 * combined to create SQL statements
 * 
 * @author Steve Weiss
 */
public abstract class SQL implements java.io.Serializable {

    /** Symbolic constant which indicates SQL "=" */
    public static final int EQ = 0;
    /** Symbolic constant which indicates SQL "=" */
    public static final int EQUALS = 0;
    /** Symbolic constant which indicates SQL "LIKE" */
    public static final int LIKE = 1;
    /** Symbolic constant which indicates SQL "!=" */
    public static final int NE = 2;
    /** Symbolic constant which indicates SQL "!=" */
    public static final int NOT_EQUAL = 2;
/** Symbolic constant which indicates SQL "<" */
    public static final int LT = 3;
    public static final int LTE = 9;
/** Symbolic constant which indicates SQL "<" */
    public static final int LESS_THAN = 3;
    /** Symbolic constant which indicates SQL ">" */
    public static final int GT = 4;
    public static final int GTE = 10;
    /** Symbolic constant which indicates SQL ">" */
    public static final int GREATER_THAN = 4;
    /** Symbolic constant which indicates SQL "LIKE 'xxx%'" */
    public static final int BEGINS = 5;
    /** Symbolic constant which indicates SQL "LIKE '%xxx'" */
    public static final int ENDS = 6;
    /** Symbolic constant which indicates SQL "IN" */
    public static final int IN = 7;
    /** Symbolic constant which indicates SQL "NOT" */
    public static final int NOT = 12;
    /**
     * Symbolic constant which indicates "=" applied to a column name, rather
     * than a value
     */
    public static final int EQUALS_COL = 8;
    /**
     * Symbolic constant which joins two columns, this is the same as "
     * <code>EQUALS_COL</code>"
     */
    public static final int JOIN = 8;

    /**
     * Symbolic constant which indicates to use regular expression for word
     * boundaries. Note: this is implemented differently in different database
     * products so we have chosen to code for this using the Strategy pattern
     * (see DBStrategy implementations). "<code>REGEXP_WB</code>"
     */
    public static final int REGEXP_WB = 11;

    /** @deprecated */
    @Deprecated
    public static final String SELECT = "SELECT";
    /** @deprecated */
    @Deprecated
    public static final String AND = "AND";
    /** @deprecated */
    @Deprecated
    public static final String OR = "OR";
    /** @deprecated */
    @Deprecated
    public static final String WHERE = "WHERE";
    /** @deprecated */
    @Deprecated
    public static final String ORDER_BY = "ORDER BY";
    /** @deprecated */
    @Deprecated
    public static final String GROUP_BY = "GROUP BY";

    protected List items = null;

    /** Used to build the actual SQL statement */
    protected StringBuffer workbuf = null;

    // LogManager _logMgr;
    String _myName = "SQL.java, ";

    // Turn off debugging by default
    boolean debug = false;

    /** Default, empty constructor */
    protected SQL() {
        init(null);
    }

    /**
     * Constructor which initializes the internal buffer with the appropriate
     * keyword
     * 
     * @param <code>keyword</code> a <code>String</code> which is appended to
     *        the clause's internal buffer, and which corresponds to a SQL
     *        keyword (i.e., 'SELECT', 'WHERE', etc)
     */
    protected SQL(String keyword) {
        init(keyword);
    }

    protected SQL(String keyword, List list) {

        this(keyword);
        setItems(list);
    }

    /**
     * Performs initialization of the clause's internal
     * <code>StringBuffer</code>
     * 
     * @param <code>keyword</code> a <code>String</code> which is appended to
     *        the clause's internal buffer, and which corresponds to a SQL
     *        keyword (i.e., 'SELECT', 'WHERE', etc)
     */
    void init(String keyword) {

        // _logMgr = LogManager.getLogManager();

        Class c = this.getClass();
        String name = c.getName();
        int idx = name.lastIndexOf('.', name.length() - 1) + 1;
        _myName = name.substring(idx, name.length()) + ".java";

        items = new ArrayList();
        workbuf = new StringBuffer();

        if (keyword != null) {
            workbuf.append(keyword);
            workbuf.append(' ');
        }
    }

    protected void setItems(List list) {
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    /**
     * Appends a <code>String</code> onto the internal buffer
     * 
     * @param <code>s</code> the <code>String</code> to be added to the SQL
     */
    protected void append(String s) {
        workbuf.append(s);
    }

    /**
     * Appends a <code>char</code> onto the internal buffer
     * 
     * @param <code>c</code> the <code>char</code> to be added to the SQL
     */
    protected void append(char c) {
        workbuf.append(c);
    }

    /**
     * Returns the content of the internal buffer as a <code>String</code>
     * 
     * @return <code>String</code>
     */
    @Override
    public String toString() {
        return new String(workbuf);
    }

    /**
     * Child classes should define this method to suite their own needs
     * 
     * @return <code>String</code>
     */
    public abstract String getContent();
}
