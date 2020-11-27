package ru.ineureka.patents.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public final class ValidationError extends BaseApiError {
    private static final long serialVersionUID = 8664295913051007963L;

    public ValidationError(String message) {
        super(message);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("e-3", "Проверка, " + getMessage());
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }
}

