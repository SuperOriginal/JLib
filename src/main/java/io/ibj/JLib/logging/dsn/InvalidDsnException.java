package io.ibj.JLib.logging.dsn;

/**
 * Exception thrown whenever the given {@link io.ibj.JLib.logging.dsn.Dsn} as been detected as invalid.
 * <p>
 * The invalidity of the DSN can either be due on the content of the DSN (invalid or missing parameters) or the sentry
 * server issuing an authentication error.
 * </p>
 */
public class InvalidDsnException extends RuntimeException {
    //CHECKSTYLE.OFF: JavadocMethod
    public InvalidDsnException() {
    }

    public InvalidDsnException(String message) {
        super(message);
    }

    public InvalidDsnException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDsnException(Throwable cause) {
        super(cause);
    }
    //CHECKSTYLE.ON: JavadocMethod
}
