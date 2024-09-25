package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Integer filmId) {
        super(message);
    }
}
