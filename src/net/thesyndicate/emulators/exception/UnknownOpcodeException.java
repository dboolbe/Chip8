package net.thesyndicate.emulators.exception;

public class UnknownOpcodeException extends EmulatorException {

    public UnknownOpcodeException() {
        super();
    }

    public UnknownOpcodeException(String message) {
        super(message);
    }

    public UnknownOpcodeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UnknownOpcodeException(Throwable throwable) {
        super(throwable);
    }
}
