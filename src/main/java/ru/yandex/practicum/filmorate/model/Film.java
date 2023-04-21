package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.DateReleaseIsValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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
