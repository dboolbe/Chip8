package net.thesyndicate.emulators.exception;

public class StackOverflowException  extends EmulatorException {

    public StackOverflowException() {
        super();
    }

    public StackOverflowException(String message) {
        super(message);
    }

    public StackOverflowException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public StackOverflowException(Throwable throwable) {
        super(throwable);
    }
}
