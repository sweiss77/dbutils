
// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;

/**
 * Constructs the "SELECT" clause of a SQL query
 * @author Steve Weiss
 */
public class SelectClause extends SQL {

    boolean _distinct = false;

    /** Default constructor */
    public SelectClause() {
        super( "SELECT" );
    }

    /**
     * Constructor which adds one item (column) to the select clause
     * @param <code>s</code> the name of the column
     */
    public SelectClause( String s ) {
        this();
        items.add( s );
    }

    public SelectClause( String[] s, boolean distinct ) {
        this( s );
        setDistinct( distinct );
    }

    /**
     * Constructor which adds an array of column names to the select clause
     * @param <code>s</code> an array of type <code>String</code>, each of
     *        which is the name of a column to be included in the SELECT
     *        statement
     */
    public SelectClause( String[] columns ) {

        this();
        for ( int i = 0; i < columns.length; i++ ) {
            items.add( columns[i] );
        }
    }

     /**
     * Constructor which adds an array of column names to the select clause
     * @param <code>columns</code> an <code>ArrayList</code> of type
     *        <code>String</code>, each of which is the name of a column to be
     *        included in the SELECT statement
     */
    public SelectClause( ArrayList columns ) {

        this();
        setItems( columns );
    }

   /** Adds the "DISTINCT" keyword to this select clause. */
    public void setDistinct() {
        setDistinct( true );
    }

    /**
     * Indicates whether or not the "DISTINCT" keyword should be added to
     * this select clause
     * @param <code>b</code> a <code>boolean</code>, if <code>true</code>
     *        the "DISTINCT" keyword is added to the clause
     */
    public void setDistinct( boolean b ) {
        _distinct = b;
    }

    /**
     * Adds a field (column) to the SELECT clause
     * @param <code>item</code> the name of the column
     */
    public void add( String item ) {
        items.add( item );
    }

    /**
     * Adds a field (column) in a specific postion to the SELECT clause
     * @param <code>item</code> the name of the column
     * @param <code>position</code> the position in the array of items to
     *        be selected at which this item should be added
     */
    public void add( String item, int position ) {
        try {
            items.add( position, item );
        } catch ( IndexOutOfBoundsException iobx ) {
            // Resize the ArrayList
        }
    }

    /**
     * Returns a <code>String</code> which contains the SQL for the "SELECT"
     * portion of a SQL query
     * @return <code>String</code>
     */
    public String getContent() {
        return getSQL();
    }

    /**
     * Returns a <code>String</code> which contains the SQL for the "SELECT"
     * portion of a SQL query
     * @return <code>String</code>
     */
    public String getSQL() {

        int i = 0;

        for ( i = 0; i < items.size(); i++ ) {

            if ( _distinct && i == 0 ) {
                append( "DISTINCT(" + items.get( i ).toString() + ")" );
            } else {
                append( items.get( i ).toString() );
            }
            if ( i < items.size() -1 ) append( ", " );

        }

        return toString();
    }
}
