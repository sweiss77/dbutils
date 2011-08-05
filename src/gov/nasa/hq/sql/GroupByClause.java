// $Id$

package gov.nasa.hq.sql;

import java.util.List;

/**
 * Creates a SQL "GROUP BY" clause
 */
public class GroupByClause extends SQL {

    public GroupByClause() {

        super("GROUP BY");
    }

    public GroupByClause(String s) {

        this();
        append(s);
    }

    public GroupByClause(List columns) {

        this();
        String[] cols = new String[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                append(", ");
            }
            cols[i] = (String) columns.get(i);
            append(cols[i]);
        }
    }

    public GroupByClause(String[] s) {

        this();

        for (int i = 0; i < s.length; i++) {
            if (i > 0) {
                append(", ");
            }
            append(s[i]);
        }
    }

    @Override
    public String getContent() {

        return toString();
    }
}
