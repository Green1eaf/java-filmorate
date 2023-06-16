package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingStorage {

    Mpa get(long id);

    List<Mpa> getAll();
}
