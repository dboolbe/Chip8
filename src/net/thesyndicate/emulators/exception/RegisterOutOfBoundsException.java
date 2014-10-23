package net.thesyndicate.emulators.exception;

public class RegisterOutOfBoundsException  extends EmulatorException {

    public RegisterOutOfBoundsException() {
        super();
    }

    public RegisterOutOfBoundsException(String message) {
        super(message);
    }

    public RegisterOutOfBoundsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RegisterOutOfBoundsException(Throwable throwable) {
        super(throwable);
    }
}
