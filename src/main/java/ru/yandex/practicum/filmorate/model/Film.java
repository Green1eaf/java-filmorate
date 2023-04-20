package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.DateReleaseIsValid;

import java.time.LocalDate;

@Data
public class Film {

    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateReleaseIsValid
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
