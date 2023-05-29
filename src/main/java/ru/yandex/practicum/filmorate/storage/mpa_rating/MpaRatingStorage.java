package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingStorage {
    void add(MpaRating mpaRating);

    void update(MpaRating mpaRating);

    MpaRating get(long id);

    void delete(long id);

    List<MpaRating> getAll();
}
