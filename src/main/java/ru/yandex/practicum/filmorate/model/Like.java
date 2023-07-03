package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @NotNull
    public Long userId;

    @NotNull
    public Long filmId;

    @NotNull
    @Range(min = 1, max = 10)
    public Long mark;
}
