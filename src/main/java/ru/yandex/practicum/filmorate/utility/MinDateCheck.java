package ru.yandex.practicum.filmorate.utility;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotations.MinFilmDate;

import java.time.LocalDate;

public class MinDateCheck implements ConstraintValidator<MinFilmDate, LocalDate> {
    private LocalDate minimalDate;

    @Override
    public void initialize(MinFilmDate constraintAnnotation) {
        minimalDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && !localDate.isBefore(minimalDate);
    }
}
