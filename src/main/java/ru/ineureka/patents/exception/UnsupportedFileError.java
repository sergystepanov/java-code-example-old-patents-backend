package ru.ineureka.patents.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class UnsupportedFileError extends BaseApiError {
    private static final long serialVersionUID = 7422565538450124850L;
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
