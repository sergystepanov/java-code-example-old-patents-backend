package ru.ineureka.patents.exception;


import ru.ineureka.patents.service.ApiErrorDto;

public final class AuthenticationError extends BaseApiError {
    private static final long serialVersionUID = 2198503335218790852L;

    public AuthenticationError(String message) {
        super(message);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("a-1", "Ошибка при аутентификации.");
    }

    @Override
    public int getHttpStatus() {
        return 401;
    }
}
