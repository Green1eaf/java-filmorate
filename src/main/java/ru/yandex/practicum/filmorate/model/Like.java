package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Like {

    private Long id;

    private Long userId;

    private Long filmId;

    @Range(min = 1, max = 10)
    private double mark;
}
