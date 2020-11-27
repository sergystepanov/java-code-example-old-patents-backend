package ru.ineureka.patents.service.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class RemoteRegistryRequestError extends BaseApiError {

    private static final long serialVersionUID = -3118500287512266863L;

    public RemoteRegistryRequestError(String error) {
        super(error);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("a-33", "Ошибка при запросе к реестру патентов (" + getMessage() + ").");
    }

    @Override
    public int getHttpStatus() {
        return 500;
    }
}
