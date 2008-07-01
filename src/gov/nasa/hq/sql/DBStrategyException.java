// $Id$
package gov.nasa.hq.sql;

/**
 * Exception class which is thrown by all DBStrategy classes
 */
public class DBStrategyException extends Exception {

    public DBStrategyException ( String message ) {
        super( message );
    }

    public DBStrategyException ( String message, Throwable t ) {
        super( message, t );
    }
}