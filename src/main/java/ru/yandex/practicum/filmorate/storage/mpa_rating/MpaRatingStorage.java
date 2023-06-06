package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingStorage {
    void add(Mpa mpa);

    void update(Mpa mpa);

    Mpa get(long id);

    void delete(long id);

    List<Mpa> getAll();
}
