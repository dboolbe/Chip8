package net.thesyndicate.emulators.exception;

public class MemoryOutOfBoundsException  extends EmulatorException {

    public MemoryOutOfBoundsException() {
        super();
    }

    public MemoryOutOfBoundsException(String message) {
        super(message);
    }

    public MemoryOutOfBoundsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MemoryOutOfBoundsException(Throwable throwable) {
        super(throwable);
    }
}
