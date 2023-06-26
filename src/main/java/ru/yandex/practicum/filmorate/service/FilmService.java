package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final DirectorService directorService;
    private final GenreService genreService;

    public FilmService(FilmStorage filmStorage,
                       UserService userService,
                       GenreService genreService,
                       DirectorService directorService
                       ) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.directorService = directorService;
        this.genreService = genreService;

    }

    public List<Film> findAll() {
        log.info("Find all films");
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (film.getId() != null) {
            throw new AlreadyExistException(
                    "Film " + film.getName() + " with id=" + film.getId() + " is already exist");
        }
        filmStorage.create(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }


    public List<Film> findFilteredPopularFilms(Integer limit, Long genreId, Integer year) {
        log.info("find " + limit + " most popular films with genreId = " + genreId + " and release year = " + year);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::getRate).thenComparing(Film::getId).reversed())
                .limit(limit)
                .filter(film -> genreId == null || film.getGenres().contains(genreService.get(genreId)))
                .filter(film -> year == null || film.getReleaseDate().getYear() == year)

                .collect(Collectors.toList());
    }

    public List<Film> searchFilms(String query, String directorAndTitle) {
        return (query == null && directorAndTitle == null) ? findFilteredPopularFilms(null, null, null)
                : filmStorage.searchFilms(query, directorAndTitle).stream()
                .sorted(Comparator.comparing(Film::getRate).thenComparing(Film::getId).reversed())
                .collect(Collectors.toList());
    }

    public void checkExisting(long id) {
        getById(id);
    }

    public Film getById(long id) {
        log.info("Get film with id=" + id);
        return Optional.ofNullable(filmStorage.get(id))
                .orElseThrow(() -> new NotExistException("Film with id=" + id + " not exists"));
    }

    public Film update(Film film) {
        log.info("Film with id=" + film.getId() + " and name=" + film.getName() + " updated");
        return filmStorage.update(film);
    }

    public void removeById(long filmId) {
        filmStorage.delete(filmId);
        log.info("remove film with id={}", filmId);
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userService.get(userId);
        userService.get(friendId);
        log.info("get common films for users with id={} and id={}", userId, friendId);
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getFilmsByDirector(long id, String sortBy) {
        directorService.getById(id);
        switch (sortBy) {
            case "year":
                return filmStorage.getFilmsByDirector(id, sortBy + "s");
            case "likes":
                return filmStorage.getFilmsByDirector(id, sortBy);
            default:
                throw new BadRequestException("Некорректный параметр сортировки: " + sortBy);
        }
    }
}