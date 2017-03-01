package net.faellr.party.api.exceptions;

public class PartyRuntimeException extends RuntimeException {
    public PartyRuntimeException(String message) {
        super(message);
    }

    public PartyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartyRuntimeException(Throwable cause) {
        super(cause);
    }

    public PartyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}