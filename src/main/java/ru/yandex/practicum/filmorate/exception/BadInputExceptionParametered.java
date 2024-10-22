package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class BadInputExceptionParametered extends IllegalArgumentException {
    private final String parameter;
    private final String reason;

    public BadInputExceptionParametered(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }
}
