// $Id$
package gov.nasa.hq.sql.connection;

public class ConnectionProviderException extends Exception {

    public ConnectionProviderException(String message) {

        super(message);
    }

    public ConnectionProviderException(Throwable t) {

        super(t);
    }

    public ConnectionProviderException(String message, Throwable t) {

        super(message, t);
    }

}
