// $Id$

package gov.nasa.hq.sql;

/**
 * The <code>WhereClause</code> class represents the WHERE portion of a SQL
 * statement
 * 
 * @author Steve Weiss
 */
public class WhereClause extends QueryFilter {

    /** Default empty constructor */
    public WhereClause() {
        super("WHERE");
    }

    /**
     * Constructor which assigns the name of the column to be included in the
     * selection criteria, the value against which that column should be
     * compared, and the type of comparison to be performed
     * 
     * @param <code>fieldname</code> the name of the column
     * @param <code>value</code> the value to which other values for the column
     *        should be compared
     * @param <code>comparator</code> a symbolic constant indicating the type of
     *        comparison to be performed
     */
    public WhereClause(String fieldname, Object value, int comparator) {
        this();
        setFilter(fieldname, value, comparator);
    }

    /**
     * Constructor which assigns the name of the column to be included in the
     * selection criteria, the value against which that column should be
     * compared, and the type of comparison to be performed
     * 
     * @param <code>fieldname</code> the name of the column
     * @param <code>value</code> the value to which other values for the column
     *        should be compared
     * @param <code>comparator</code> a symbolic constant indicating the type of
     *        comparison to be performed
     */
    public WhereClause(String fieldname, Object value, int comparator, boolean flag) {
        this();
        setCaseSensitive(flag);
        setFilter(fieldname, value, comparator);
    }

    /**
     * Adds an <code>AndClause</code> to this <code>WhereClause</code>
     * 
     * @param <code>andClause</code> the <code>AndClause</code>
     */
    public void addAndClause(AndClause andClause) {
        addClause(andClause);
    }

    /**
     * Adds an <code>OrClause</code> to this <code>WhereClause</code>
     * 
     * @param <code>orClause</code> the <code>OrClause</code>
     */
    public void addOrClause(OrClause orClause) {
        addClause(orClause);
    }

    /**
     * Returns a <code>String</code> containing the WHERE clause of a SQL
     * statement
     * 
     * @return <code>String</code>
     */
    public String getSQL() {
        return getContent();
    }
}
