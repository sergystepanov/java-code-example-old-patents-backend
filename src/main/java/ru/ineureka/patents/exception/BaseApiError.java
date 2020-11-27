package ru.ineureka.patents.exception;


import ru.ineureka.patents.service.ApiErrorDto;

public class BaseApiError extends RuntimeException implements ApiError {
    private static final long serialVersionUID = -3118500287512266863L;

    BaseApiError(String message) {
        super(message);
    }

    BaseApiError(Throwable cause, String message) {
        super(message, cause);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("0", "Неизвестная ошибка.");
    }

    @Override
    public int getHttpStatus() {
        return 500;
    }
}
