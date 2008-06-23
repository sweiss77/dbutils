// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;

/**
 * <code>QueryFilter</code> is an abstract class which is the base class for all
 * classes that represent filters for a SQL query.
 * @author Steve Weiss
 */
public class QueryFilter implements java.io.Serializable {

    String colname = null;
    String value = null;
    int comparator;
    ArrayList clauses = null;
    boolean caseSensitive = true;
    String _keyword = null;
    boolean identity_ = false;
    DBStrategy dbstrategy = null;

    protected ArrayList items = null;

    /** Default, empty constructor */
    protected QueryFilter() {

        clauses = new ArrayList();
        items = new ArrayList();
        caseSensitive = true;
    }

    /**
     * Constructor which initializes the internal <code>StringBuffer</code>
     * with the appropriate SQL keyword
     * @param <code>keyword</code> a <code>String</code> holding the SQL keyword
     */
    protected QueryFilter( String keyword ) {

        this();
        _keyword = keyword;
    }

    protected void setKeyword( String keyword ) {

        _keyword = keyword.toUpperCase();
    }

    protected void setItems( ArrayList list ) {

        for ( int i = 0; i < list.size(); i++ ) {
            items.add( list.get( i ) );
        }
    }

    public void setCaseSensitive( boolean flag ) {

        caseSensitive = flag;
    }

    public boolean getCaseSensitive() {

        return caseSensitive;
    }

    /**
     * Returns the name of the database column for this filter
     * @return <code>String</code>
     */
    public String getColumnName() {

        return colname;
    }

    /**
     * Returns the value which is to be applied to the filter
     * @return <code>String</code>
     */
    public String getValue() {

        return value;
    }

    /**
     * Returns the type of comparison to be performed by this filter.
     * Comparators are defined as symbolic constants in the <code>SQL</code>
     * parent class
     * @return <code>int</code>
     */
    public int getComparator() {

        return comparator;
    }

    /**
     * Adds a filter (sub-clause) to this filter. This allows selection criteria
     * to be nested in a SQL query
     * @param <code>filter</code> a instance of a class which extends
     *        <code>QueryFilter</code>
     */
    public void addFilter( QueryFilter filter ) {

        clauses.add( filter );
    }

    /**
     * Adds a filter (sub-clause) to this filter. This allows selection criteria
     * to be nested in a SQL query. This function simply calls
     * <code>addFilter</code>, there is no difference in the two functions.
     * @param <code>clause</code> a instance of a class which extends
     *        <code>QueryFilter</code>
     */
    public void appendClause( QueryFilter clause ) {

        addFilter( clause );
    }

    public void addClause( QueryFilter clause ) {

        appendClause( clause );
    }

    /**
     * Defines the selection criteria for the filter
     * @param <code>fieldname</code> the name of the database column
     * @param <code>val</code> the value to be applied to the selection
     *        criteria
     * @param <code>comp</code> a symbolic constant (as defined in the
     *        <code>SQL</code> base class) which determines the type of
     *        comparison to be performed
     */
    public void setFilter( String fieldname, Object val, int comp ) {

        String func = "setFilter(String,Object,int), ";

        colname = fieldname;
        comparator = comp;

        Class c = val.getClass();
        String s = c.getName();
        
        if ( s.equals( "java.lang.String" )
            || s.equals( "java.lang.StringBuffer" ) ) {

            if ( !caseSensitive && ( comp != SQL.EQUALS_COL ) ) {
                colname = "UPPER(" + fieldname + ")";
            }

            StringBuffer buf = new StringBuffer();
            if ( comp == SQL.EQUALS_COL ) {
                value = val.toString();
            } else if( comp == SQL.REGEXP ) {
            	value = dbstrategy.getRegexp(fieldname, val, caseSensitive);
            }else {
                buf.append( "'" );
                if ( comp == SQL.LIKE || comp == SQL.ENDS )
                    buf.append( '%' );
                if ( !caseSensitive ) {
                    buf.append( val.toString().toUpperCase() );
                } else {
                    buf.append( val.toString() );
                }
                if ( comp == SQL.LIKE || comp == SQL.BEGINS )
                    buf.append( '%' );
                buf.append( "'" );
                value = new String( buf );
            }

        } else {
            value = val.toString();
        }

    }

    /**
     * Returns the number of sub-clauses this filter has
     * @return <code>int</code>
     */
    public int numClauses() {

        return clauses.size();
    }

    /**
     * Returns a <code>String</code> containing the actual SQL
     * represented by the filter
     * @return <code>String</code>
     */
    public String toString() {

        return getContent();
    }

    public String getContent() {

        StringBuffer workbuf = new StringBuffer();

        if ( _keyword != null ) {
            workbuf.append( _keyword );
            workbuf.append( ' ' );
        }

        if ( clauses.size() > 0 )
            workbuf.append( " (" );

        if ( identity_ ) {

            workbuf.append( "1 = 1" );

        } else {

           // workbuf.append( colname );

            switch ( comparator ) {

                case SQL.EQ:
                    workbuf.append( colname + " = " + value );
                    break;

                case SQL.LIKE:
                    workbuf.append( colname +  " LIKE " + value );
                    break;

                case SQL.NE:
                    workbuf.append( colname +  " != " + value );
                    break;

                case SQL.GT:
                    workbuf.append( colname +  " > " + value );
                    break;

                case SQL.GTE:
                    workbuf.append( colname +  " >= " + value );
                    break;

                case SQL.LT:
                    workbuf.append( colname +  " < " + value );
                    break;

                case SQL.LTE:
                    workbuf.append( colname +  " <= " + value );
                    break;

                case SQL.BEGINS:
                    workbuf.append( colname +  " LIKE " + value );
                    break;

                case SQL.ENDS:
                    workbuf.append( colname +  " LIKE " + value );
                    break;

                case SQL.IN:
                    workbuf.append( colname +  " IN " + value.toString() );
                    break;

                case SQL.EQUALS_COL:
                    workbuf.append( colname +  " = " + value.toString() );
                    break;
                    
                case SQL.REGEXP:  //regular expression for word boundaries
                	workbuf.append(value);
                	break;
            }

        }

        for ( int i = 0; i < clauses.size(); i++ ) {

            workbuf.append( ' ' );
            QueryFilter qf = (QueryFilter) clauses.get( i );
            workbuf.append( qf.getContent() );
            //if ( i < clauses.size() - 1 ) workbuf.append( ' ' );
        }

        if ( clauses.size() > 0 )
            workbuf.append( ")" );

        return new String( workbuf );
    }

    public void setIdentity( boolean identity ) {

        identity_ = identity;
    }

    public boolean getIdentity() {

        return identity_;
    }
    
    /**
     * Defines the DBStrategy for the filter
     * @param <code>dbStrategy</code> the DBStrategy passed in from the client
     */
    public void setDbStrategy( DBStrategy dbstrategy ) {
       this.dbstrategy = dbstrategy;
    }
    
 
        
}
