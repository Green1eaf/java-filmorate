package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    void add(Genre genre);

    void update(Genre genre);

    Genre get(long id);

    void delete(long id);

    List<Genre> getAll();
}
