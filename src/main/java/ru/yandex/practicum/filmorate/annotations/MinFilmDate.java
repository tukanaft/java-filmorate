package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import ru.yandex.practicum.filmorate.utility.MinDateCheck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinDateCheck.class)
public @interface MinFilmDate {
    String message() default "Минимальная дата выпуска фильма не может быть ранее {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}