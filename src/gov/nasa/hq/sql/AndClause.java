// $Id$

package gov.nasa.hq.sql;

public class AndClause extends QueryFilter implements java.io.Serializable {

    public AndClause() {
        super("AND");
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
    public AndClause(String fieldname, Object value, int comparator, boolean flag) {
        this();
        setCaseSensitive(flag);
        setFilter(fieldname, value, comparator);
    }

    /**
     * Adding constructors to pass in a DBStrategy object (Strategy pattern)
     * Concrete strategies such as OracleStrategy and MySqlStrategy should
     * implement the methods of the DBStrategy interface.
     * 
     * @param <code>dbStrtaegy</code> the DBStrategy passed in from the client
     */
    public AndClause(String fieldname, Object value, int comparator,
                     DBStrategy dbstrategy) {
        this(dbstrategy);
        setFilter(fieldname, value, comparator);
    }

    public AndClause(String fieldname, Object value, int comparator,
                     DBStrategy dbstrategy, boolean flag) {
        this(dbstrategy);
        setCaseSensitive(flag);
        setFilter(fieldname, value, comparator);
    }

    public AndClause(DBStrategy dbstrategy) {
        this();
        setDbStrategy(dbstrategy);
    }

    public AndClause(String fieldname, Object value, int comparator) {

        this();
        setFilter(fieldname, value, comparator);
    }

    public AndClause(String fieldname, Object value, int comparator,
                     AndClause andClause) {

        this(fieldname, value, comparator);
        addAndClause(andClause);
    }

    public AndClause(String fieldname, Object value, int comparator,
                     AndClause[] andClause) {

        this(fieldname, value, comparator);

        for (int i = 0; i < andClause.length; i++) {
            addAndClause(andClause[i]);
        }

    }

    public void addAndClause(AndClause andClause) {
        addClause(andClause);
    }

    public void addOrClause(OrClause orClause) {
        addClause(orClause);
    }

    public String getSQL() {
        return getContent();
    }
}
