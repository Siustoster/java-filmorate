package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationExcepton;

import java.util.Map;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")

public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleExceptionNotFound(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(ValidationExcepton.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleExceptionValidation(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleExceptionOther(final RuntimeException e) {
        return Map.of(
                "error", "Ошибка при выполнении запроса",
                "errorMessage", e.getMessage()
        );
    }
}
