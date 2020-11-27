package ru.ineureka.patents.exception;


import ru.ineureka.patents.service.ApiErrorDto;

public final class DuplicateValueError extends BaseApiError {
    private static final long serialVersionUID = 7522173522912235565L;
    private final String value;

    public DuplicateValueError(String value) {
        super(value);

        this.value = value;
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("d-1", "Дубликат значения: " + value + ".");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
