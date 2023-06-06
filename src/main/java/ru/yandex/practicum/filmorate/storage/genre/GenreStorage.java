package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    void add(long filmId, List<Genre> genres);

    void update(long filmId, List<Genre> genres);

    Genre get(long id);

    void delete(long filmId);

    List<Genre> getAll();

    List<Genre> getAllByFilmId(long id);
}
