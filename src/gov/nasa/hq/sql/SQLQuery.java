// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;

/**
 * The <code>SQLQuery</code> class is used to generate SQL queries
 * @author Steve Weiss
 */
public class SQLQuery implements java.io.Serializable {

    SelectClause _select = null;
    FromClause _from = null;
    WhereClause _where = null;
    AndClause _and = null;
    OrClause _or = null;
    OrderByClause _orderBy = null;
    GroupByClause _groupBy = null;

    ArrayList _clauses = null;

    /** Default empty constructor, initializes the <code>FromClause</code> and
     * the <code>WhereClause</code> portion of the query
     */
    public SQLQuery() {

        init( new SelectClause(),
              new FromClause(),
              null,
              null,
              null,
              null,
              null );
    }

    public SQLQuery( SelectClause select,
                     FromClause from,
                     WhereClause where,
                     AndClause and,
                     OrClause or,
                     OrderByClause orderBy,
                     GroupByClause groupBy ) {

        init( select, from, where, and, or, orderBy, groupBy );
    }

    public SQLQuery( SelectClause select,
                     FromClause from,
                     WhereClause where,
                     AndClause and,
                     OrClause or ) {

        init( select, from, where, and, or, null, null );
    }

    public SQLQuery( SelectClause select,
                     FromClause from,
                     WhereClause where,
                     AndClause and ) {

        init( select, from, where, and, null, null, null );
    }

    public SQLQuery( SelectClause select,
                     FromClause from,
                     WhereClause where,
                     OrClause or ) {

        init( select, from, where, null, or, null, null );
    }

    public SQLQuery( SelectClause select, FromClause from, WhereClause where ) {

        init( select, from, where, null, null, null, null );
    }

    public SQLQuery( SelectClause select, FromClause from ) {

        init( select, from, null, null, null, null, null );
    }

    public SQLQuery( String[] cols, String[] tables ) {

        init( new SelectClause( cols ),
              new FromClause( tables ),
              null,
              null,
              null,
              null,
              null );
    }

    public SQLQuery( String[] cols, String[] tables, WhereClause where ) {

        init( new SelectClause( cols ),
              new FromClause( tables ),
              where,
              null,
              null,
              null,
              null );
    }

    public SQLQuery( String[] cols, String table, WhereClause where ) {

        init( new SelectClause( cols ),
              new FromClause( table ),
              where,
              null,
              null,
              null,
              null );
    }

    void init( SelectClause select,
               FromClause from,
               WhereClause where,
               AndClause and,
               OrClause or,
               OrderByClause orderBy,
               GroupByClause groupBy ) {

        _clauses = new ArrayList();

        if ( select != null )
            _select = select;
        if ( from != null )
            _from = from;
        if ( where != null )
            _where = where;
        if ( and != null )
            _and = and;
        if ( or != null )
            _or = or;
        if ( orderBy != null )
            _orderBy = orderBy;
        if ( groupBy != null )
            _groupBy = groupBy;

    }

    public void addFilter( QueryFilter filter ) {

        _clauses.add( filter );
    }

    public void setSelectClause( SelectClause select ) {

        _select = select;
    }

    public void setFromClause( FromClause from ) {

        _from = from;
    }

    public void setWhereClause( WhereClause where ) {

        _where = where;
    }

    public void setAndClause( AndClause andClause ) {

        //_and = and;
        _clauses.add( andClause );
    }

    public void setOrClause( OrClause orClause ) {

        //_or = orClause;
        _clauses.add( orClause );
    }

    public void setOrderByClause( OrderByClause orderByClause ) {

        _orderBy = orderByClause;
    }

    public void setGroupByClause( GroupByClause groupByClause ) {

        _groupBy = groupByClause;
    }

    public String getSQL() {

        return toString();
    }

    public String toString() {

        StringBuffer work = new StringBuffer();

        if ( _select != null ) {
            work.append( _select.getContent() );
            work.append( ' ' );
        }

        if ( _from != null ) {
            work.append( _from.getContent() );
            work.append( ' ' );
        }

        if ( _where != null ) {
            work.append( _where.getContent() );
            work.append( ' ' );
        }

        if ( _clauses != null ) {

            for ( int i = 0; i < _clauses.size(); i++ ) {

                QueryFilter qf = (QueryFilter) _clauses.get( i );
                work.append( qf.getContent() );
                if ( i < _clauses.size() - 1 )
                    work.append( ' ' );
            }

        }

        // The GROUP BY clause *must* come before the ORDER BY clause
        if ( _groupBy != null ) {
            work.append( ' ' );
            work.append( _groupBy.getContent() );
        }

        if ( _orderBy != null ) {
            work.append( ' ' );
            work.append( _orderBy.getContent() );
        }

        return ( new String( work ) );
    }
}
