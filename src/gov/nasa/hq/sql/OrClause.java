
// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;

/**
 * Creates a SQL "OR" clause
 */
public class OrClause extends QueryFilter {

    public OrClause() {
        super( "OR" );
    }

    /** Adding constructors to pass in a DBStrategy object (Strategy pattern)
     * Concrete strategies such as OracleStrategy and MySqlStrategy should implement 
     * the methods of the DBStrategy interface.
     * @param <code>dbStrtaegy</code> the DBStrategy passed in from the client
     */    
    public OrClause( DBStrategy dbstrategy ) {
        this();
        setDbStrategy( dbstrategy );
    }

    public OrClause( String fieldname, Object value,
            int comparator, DBStrategy dbstrategy ) {
        this(dbstrategy);
        setFilter( fieldname, value, comparator );
    }
    
    public OrClause( String fieldname, Object value,
            int comparator, DBStrategy dbstrategy, boolean flag ) {
        this(dbstrategy);
        setCaseSensitive( flag );
        setFilter( fieldname, value, comparator );
    }
    
    public OrClause( String fieldname, Object value, int comparator ) {

        this();
        setFilter( fieldname, value, comparator );

    }

    /**
     * Constructor which assigns the name of the column to be included in the
     * selection criteria, the value against which that column should be
     * compared, and the type of comparison to be performed
     * @param <code>fieldname</code> the name of the column
     * @param <code>value</code> the value to which other values for the
     *        column should be compared
     * @param <code>comparator</code> a symbolic constant indicating the
     *        type of comparison to be performed
     */
    public OrClause( String fieldname, Object value,
                     int comparator, boolean flag ) {

        this();
        setCaseSensitive( flag );
        setFilter( fieldname, value, comparator );

    }

    public OrClause( String fieldname, Object value, int comparator,
                     OrClause andClause ) {

        this( fieldname, value, comparator );
        addOrClause( andClause );
    }

    public OrClause( String fieldname, Object value, int comparator,
                     OrClause[] andClause ) {

        this( fieldname, value, comparator );

        for ( int i = 0; i < andClause.length; i++ ) {
            addOrClause( andClause[i] );
        }

    }

    public void addAndClause( AndClause andClause ) {
        addFilter( andClause );
    }

    public void addOrClause( OrClause orClause ) {
        addFilter( orClause );
    }

    public String getSQL() {
        return getContent();
    }

}
