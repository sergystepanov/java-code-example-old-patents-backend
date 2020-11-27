package ru.ineureka.patents.exception;


import ru.ineureka.patents.service.ApiErrorDto;

public class FileError extends BaseApiError {
    private static final long serialVersionUID = -6194534727144971681L;
    private final String value;

    public FileError() {
        super("");

        this.value = "";
    }

    public FileError(String value) {
        super(value);

        this.value = value;
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("f-1", "Ошибка с файлом: " + value + ".");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
