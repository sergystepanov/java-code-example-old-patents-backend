package ru.ineureka.patents.service.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class UnsupportedFileError extends BaseApiError {

    private static final long serialVersionUID = -3118500287512266863L;

    private final String value;

    public UnsupportedFileError(String value) {
        super(value);

        this.value = value;
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("f-2",
                "Данный тип файлов не поддерживается" + (value.isEmpty() ? "" : ": " + value) + ".");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
