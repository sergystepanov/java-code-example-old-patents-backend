package ru.ineureka.patents.service.exception;

import ru.ineureka.patents.service.ApiErrorDto;

public interface ApiError {

    ApiErrorDto getError();

    int getHttpStatus();
}
