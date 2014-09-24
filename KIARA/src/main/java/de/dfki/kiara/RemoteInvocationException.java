package de.dfki.kiara;

public class RemoteInvocationException extends RuntimeException {

    public RemoteInvocationException() {
    }

    public RemoteInvocationException(String message) {
        super(message);
    }

    public RemoteInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteInvocationException(Throwable cause) {
        super(cause);
    }
}
