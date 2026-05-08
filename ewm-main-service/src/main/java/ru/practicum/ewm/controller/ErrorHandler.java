package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.practicum.ewm.dto.response.ApiError;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.error("404 NotFound: {}", e.getMessage());
        return createApiError(HttpStatus.NOT_FOUND, "The required object was not found.", e.getMessage());
    }


    @ExceptionHandler({
            ConflictException.class,
            DataIntegrityViolationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final Exception e) {
        log.error("409 Conflict: {}", e.getMessage());
        return createApiError(HttpStatus.CONFLICT, "For the requested operation the conditions are not met.", e.getMessage());
    }


    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            HandlerMethodValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final Exception e) {
        log.error("400 BadRequest: {}", e.getMessage());
        return createApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("500 InternalServerError: ", e);
        return createApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", e.getMessage());
    }

    private ApiError createApiError(HttpStatus status, String reason, String message) {
        return ApiError.builder()
                .status(status.name())
                .reason(reason)
                .message(message != null ? message : "No message available")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}