package net.faellr.party.api.exceptions;

public class PartyException extends Exception {
    public PartyException(String message) {
        super(message);
    }

    public PartyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartyException(Throwable cause) {
        super(cause);
    }

    public PartyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}