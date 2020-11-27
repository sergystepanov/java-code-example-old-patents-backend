package ru.ineureka.patents.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class SpreadsheetOldError extends BaseApiError {
    private static final long serialVersionUID = 1638206150121144750L;

    public SpreadsheetOldError(String message) {
        super(message);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("s-2", "Слишком старая версия файла.");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
