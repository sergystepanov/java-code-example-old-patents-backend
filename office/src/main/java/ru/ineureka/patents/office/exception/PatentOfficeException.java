package ru.ineureka.patents.office.exception;

public class PatentOfficeException extends Exception {
    private static final long serialVersionUID = 1200678749837926514L;
    private final OfficeError errorCode;

    public PatentOfficeException(OfficeError errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PatentOfficeException(String message) {
        super(message);
        this.errorCode = OfficeError.NAMED;
    }

    public OfficeError getErrorCode() {
        return errorCode;
    }
}
