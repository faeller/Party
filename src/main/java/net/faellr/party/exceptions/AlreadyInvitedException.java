package net.faellr.party.exceptions;

public class AlreadyInvitedException extends InvitationException {
    public AlreadyInvitedException(String message) {
        super(message);
    }

    public AlreadyInvitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyInvitedException(Throwable cause) {
        super(cause);
    }

    public AlreadyInvitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
