// $Id$

package gov.nasa.hq.sql;

import java.util.ArrayList;

/**
 * Creates the "FROM" clause of a SQL statement
 * 
 * @author Steve Weiss
 * @version 1.0
 */
public class FromClause extends SQL implements java.io.Serializable {

    ArrayList tables = null;

    public FromClause() {
        super("FROM");
        tables = new ArrayList();
    }

    public FromClause(String s) {
        this();
        tables.add(s);
    }

    public FromClause(String[] s) {
        this();
        for (int i = 0; i < s.length; i++) {
            tables.add(s[i]);
        }
    }

    public FromClause(ArrayList list) {
        this();
        // setItems( list );
        for (int i = 0; i < list.size(); i++) {
            tables.add(list.get(i));
        }
    }

    public void add(String item) {
        tables.add(item);
    }

    public void add(String item, int position) {
        try {
            tables.add(position, item);
        } catch (IndexOutOfBoundsException iobx) {
            // Resize the ArrayList
        }
    }

    @Override
    public String getContent() {

        int i = 0;
        for (i = 0; i < tables.size(); i++) {
            append(tables.get(i).toString());
            if (i < tables.size() - 1)
                append(", ");
        }

        // append( tables.get( i ).toString() + " " );
        return toString();
    }
}
