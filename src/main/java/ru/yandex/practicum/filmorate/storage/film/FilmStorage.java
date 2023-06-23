package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void delete(long id);

    Film get(long id);

    List<Film> findAll();

    List<Film> getFilmsByDirector(long id, String sortBy);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> searchFilms(String query, String directorAndTitle);
    List<Film> getFilmsByPartOfTitle(String filmNamePart);
    List<Film> getFilmsByPartOfDirectorName(String directorNamePart);
}
