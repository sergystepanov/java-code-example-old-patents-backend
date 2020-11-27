package ru.ineureka.patents.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class SpreadsheetError extends BaseApiError {
    private static final long serialVersionUID = 6032625039342235704L;
    private final String value;

    public SpreadsheetError(String value) {
        super(value);

        this.value = value;
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("s-1", "Ошибка при чтении таблицы: " + value + ".");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
