package net.faellr.party.api.exceptions;

public class NotOwnerException extends Exception {
    public NotOwnerException(String message) {
        super(message);
    }

    public NotOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotOwnerException(Throwable cause) {
        super(cause);
    }

    public NotOwnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}