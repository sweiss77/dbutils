// $Id$

package gov.nasa.hq.sql;

/**
 * Creates a SQL "ORDER BY" clause
 */
public class OrderByClause extends SQL {

    public OrderByClause() {
        super("ORDER BY");
    }

    public OrderByClause(String s) {
        this();
        append(s);
    }

    public OrderByClause(String[] s) {

        this();

        int i = 0;

        for (i = 0; i < s.length - 1; i++) {
            append(s[i]);
            append(", ");
        }

        append(s[i]);
    }

    @Override
    public String getContent() {
        return toString();
    }
}
