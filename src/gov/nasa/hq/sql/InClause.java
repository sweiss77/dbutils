
// $Id$

package gov.nasa.hq.sql;

import java.util.*;

/**
 * This class creates a <code>String</code> which can be used to set the value of
 * a SQL "IN" clause. It does not supply the "IN" keyword itself, that should be
 * done by using the symbolic constant <code>SQL.IN</code> as the comparator in
 * a <code>WhereClause</code>
 * @author Steve Weiss
 */
public class InClause extends SQL {

    /** Default empty constructor */
    public InClause() {
        super();
    }
    
    /**
     * Constructor which takes a string as an argument
     */
    
    public InClause( String sub ) {
        this();
        append( sub);
    }
    
    /**
     * Constructor which takes a {@link SQLQuery} object as an argument. The
     * should be used to add sub-queries to the SQL
     * @param subquery
     */
    public InClause( SQLQuery subquery ) {
        this();
        append( subquery.getSQL());
    }

    /**
     * The constructor takes an <code>ArrayList</code> of items to be contained
     * in the "IN" clause of the SQL statement. The first item in the list is
     * checked to see if it is either a <code>String</code> or
     * <code>StringBuffer</code>, if so, single quotes are added around each item
     * in the list
     * @param <code>ArrayList</code>
     */
    public InClause( ArrayList stuff ) {

        this();
        Boolean isString = null;

        // Start with an open parenthesis
        append( '(' );

        for ( int i = 0; i < stuff.size(); i++ ) {

            Object obj = stuff.get( i );

            // Check to see whether the first value in the list is a string type
            if ( isString == null ) {

                Class  c = obj.getClass();
                String s = c.getName();
                //debug( "class = " + s, 5 );

                if ( s.equals( "java.lang.String" ) ||
                     s.equals( "java.lang.StringBuffer" ) ) {
                    isString = new Boolean( true );
                } else {
                    isString = new Boolean( false );
                }
            }

            /* If the object is a character type, we surround it with single
             * quotes
             */
            if ( isString.booleanValue() ) {

                append( "'" );
                append( obj.toString() );
                append( "'" );

            } else {

                append( obj.toString() );
            }

            // If there is another item in the list, add a comma
            if ( i < stuff.size() - 1 ) append( ", " );
        }

        // Add closing parenthesis
        append( ')' );
    }

    /**
     * Returns the <code>String</code> representation of the internal buffer
     * @return <code>String</code>
     */
    public String getContent() {
        return new String( workbuf );
    }
}
