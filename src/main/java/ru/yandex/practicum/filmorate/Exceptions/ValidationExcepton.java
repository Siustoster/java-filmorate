package ru.yandex.practicum.filmorate.Exceptions;

public class ValidationExcepton extends RuntimeException {
    public ValidationExcepton(String message) {
        super(message);
    }
}
