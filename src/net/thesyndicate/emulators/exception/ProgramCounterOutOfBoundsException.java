package net.thesyndicate.emulators.exception;

public class ProgramCounterOutOfBoundsException  extends EmulatorException {

    public ProgramCounterOutOfBoundsException() {
        super();
    }

    public ProgramCounterOutOfBoundsException(String message) {
        super(message);
    }

    public ProgramCounterOutOfBoundsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ProgramCounterOutOfBoundsException(Throwable throwable) {
        super(throwable);
    }
}
