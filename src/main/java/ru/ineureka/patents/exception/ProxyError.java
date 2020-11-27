package ru.ineureka.patents.exception;


import ru.ineureka.patents.service.ApiErrorDto;

public class ProxyError extends BaseApiError {
    private static final long serialVersionUID = -1524045200013889781L;

    public ProxyError(Throwable cause, String message) {
        super(cause, message);
    }

    public ProxyError(String message) {
        super(message);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("p-1", "Ошибка прокси.");
    }
}
