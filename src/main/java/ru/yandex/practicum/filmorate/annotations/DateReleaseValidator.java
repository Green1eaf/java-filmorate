package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateReleaseValidator implements ConstraintValidator<DateReleaseIsValid, LocalDate> {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(DateReleaseIsValid dateReleaseIsValid) {
    }

    @Override
    public boolean isValid(LocalDate ld, ConstraintValidatorContext context) {
        return ld == null || ld.isAfter(MIN_RELEASE_DATE);
    }
}