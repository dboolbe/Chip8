package net.thesyndicate.emulators.exception;

public class InvalidKeyException extends EmulatorException {

    public InvalidKeyException() {
        super();
    }

    public InvalidKeyException(String message) {
        super(message);
    }

    public InvalidKeyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidKeyException(Throwable throwable) {
        super(throwable);
    }
}
