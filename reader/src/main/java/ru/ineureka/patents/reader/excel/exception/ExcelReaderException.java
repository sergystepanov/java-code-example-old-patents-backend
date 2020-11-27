package ru.ineureka.patents.reader.excel.exception;

public class ExcelReaderException extends RuntimeException {
    private static final long serialVersionUID = 1254902557531381397L;
    private final ExcelReaderExceptionType type;
    private final String message;

    public ExcelReaderException(ExcelReaderExceptionType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ExcelReaderException(ExcelReaderExceptionType type) {
        this.type = type;
        this.message = "";
    }

    public ExcelReaderExceptionType getType() {
        return type;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
