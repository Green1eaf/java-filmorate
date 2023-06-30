package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
            //можно заменить аннотацией @Range(min ="1", max = "10")
    @Min(1)
    @Max(10)
    public Long mark;
}
