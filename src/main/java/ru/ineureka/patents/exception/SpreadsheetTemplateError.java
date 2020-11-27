package ru.ineureka.patents.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public class SpreadsheetTemplateError extends BaseApiError {
    private static final long serialVersionUID = 6032625039342235704L;

    public SpreadsheetTemplateError(String value) {
        super(value);
    }

    @Override
    public ApiErrorDto getError() {
        return new ApiErrorDto("s-22", "Шаблон для клиента не найден.");
    }

    @Override
    public int getHttpStatus() {
        return 402;
    }
}
