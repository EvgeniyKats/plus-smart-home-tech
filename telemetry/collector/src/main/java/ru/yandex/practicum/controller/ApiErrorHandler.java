package ru.yandex.practicum.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final ConstraintViolationException e) {
        ApiError apiError = createApiError(e);
        loggingApiError(apiError, Level.WARN);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Throwable e) {
        ApiError apiError = createApiError(e);
        loggingApiError(apiError, Level.ERROR);
        return apiError;
    }

    private ApiError createApiError(Throwable e) {
        return new ApiError(e.getMessage(), Arrays.toString(e.getStackTrace()));
    }

    private void loggingApiError(ApiError apiError, Level level) {
        String msg = apiError.toString();
        switch (level) {
            case DEBUG -> log.debug(msg);
            case ERROR -> log.error(msg);
            case TRACE -> log.trace(msg);
            case INFO -> log.info(msg);
            case WARN -> log.warn(msg);
            default -> log.error("Unknown level: {} msg: {}", level, msg);
        }
    }
}
