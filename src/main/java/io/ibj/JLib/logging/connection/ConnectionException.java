package io.ibj.JLib.logging.connection;

/**
 * Exception thrown by a {@link io.ibj.JLib.logging.connection.Connection} if something went wrong temporarily.
 * <p>
 * This allows connections to know when to back off for a while.
 * </p>
 */
public class ConnectionException extends RuntimeException {
    //CHECKSTYLE.OFF: JavadocMethod
    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }
    //CHECKSTYLE.ON: JavadocMethod
}
