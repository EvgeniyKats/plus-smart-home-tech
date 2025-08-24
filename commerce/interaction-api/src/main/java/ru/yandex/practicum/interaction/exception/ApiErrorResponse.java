package ru.yandex.practicum.interaction.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApiErrorResponse {

    String httpStatus;
    String userMessage;

    public ApiErrorResponse(BaseServiceException serviceException) {
        this.httpStatus = serviceException.getHttpStatus();
        this.userMessage = serviceException.getUserMessage();
    }

    public ApiErrorResponse(String userMessage, String httpStatus) {
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }
}
