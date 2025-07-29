package ru.yandex.practicum.interaction.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApiErrorResponse {
    Throwable cause;

    StackTraceElement[] stackTrace;

    String httpStatus;

    String userMessage;

    String message;

    String localizedMessage;

    public ApiErrorResponse(BaseServiceException serviceException) {
        cause = serviceException;
        stackTrace = serviceException.getStackTrace();
        httpStatus = serviceException.getHttpStatus();
        userMessage = serviceException.getUserMessage();
        message = serviceException.getMessage();
        localizedMessage = serviceException.getLocalizedMessage();
    }

    public ApiErrorResponse(Throwable cause) {
        this.cause = cause;
        stackTrace = cause.getStackTrace();
        httpStatus = "500";
        userMessage = "unknown error";
        message = cause.getMessage();
        localizedMessage = cause.getLocalizedMessage();
    }
}
