package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRatingStorage {

    Optional<Mpa> get(long id);

    List<Mpa> getAll();
}
