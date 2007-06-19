
// $Id$

package gov.nasa.hq.sql;

/**
 * Creates a SQL "GROUP BY" clause
 */
public class GroupByClause extends SQL {

    public GroupByClause() {
        super( "ORDER BY" );
    }

    public GroupByClause( String s ) {
        this();
        append( s );
    }

    public GroupByClause( String[] s ) {

        this();

        int i = 0;

        for ( i = 0; i < s.length - 1; i++ ) {
            append( s[i] );
            append( ", " );
        }

        append( s[i] );
    }

    public String getContent() {
        return toString();
    }
}
