package ru.ineureka.patents.service.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public final class ValidationError extends BaseApiError {

    private static final long serialVersionUID = -3118500287512266863L;

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

