package ru.ineureka.patents.service;

public final class ApiErrorDto {

    private final String code;
    private final String message;

    public ApiErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "ApiErrorDto(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
}
