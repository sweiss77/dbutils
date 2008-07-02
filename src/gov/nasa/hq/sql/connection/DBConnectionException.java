// $Id$
package gov.nasa.hq.sql.connection;

/**
 * Exception class which is thrown by the DBConnectionStrategy classes 
 * when an unsupported connection type is specified.
 */
public class DBConnectionException extends Exception {

    public DBConnectionException ( String message ) {
        super( message );
    }

    public DBConnectionException ( String message, Throwable t ) {
        super( message, t );
    }
    
}