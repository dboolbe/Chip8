package net.thesyndicate.emulators.exception;

public abstract class EmulatorException extends Exception {

    public EmulatorException() {
        super();
    }

    public EmulatorException(String message) {
        super(message);
    }

    public EmulatorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EmulatorException(Throwable throwable) {
        super(throwable);
    }
}
