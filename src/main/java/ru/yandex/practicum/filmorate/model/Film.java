package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.DateReleaseIsValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateReleaseIsValid
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private String mpaRating;

    private final Set<Long> likes = new HashSet<>();
}