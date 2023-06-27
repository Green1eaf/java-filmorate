package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateReleaseValidator implements ConstraintValidator<DateReleaseIsValid, LocalDate> {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(DateReleaseIsValid dateReleaseIsValid) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null || date.isAfter(MIN_RELEASE_DATE);
    }
}