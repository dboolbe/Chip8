package net.thesyndicate.emulators.exception;

public class InvalidFilePathException extends EmulatorException {

    public InvalidFilePathException() {
        super();
    }

    public InvalidFilePathException(String message) {
        super(message);
    }

    public InvalidFilePathException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidFilePathException(Throwable throwable) {
        super(throwable);
    }
}
