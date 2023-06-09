package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collections;
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
        this.genreService = genreService;
        this.directorService = directorService;
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
        genreService.addAll(film.getId(), film.getGenres());
        directorService.addAllToFilm(film.getId(), film.getDirectors());
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

    public List<Film> findFilteredPopularFilms() {
        log.info("find most popular films");
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::getRate).thenComparing(Film::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<Film> searchFilmsByQuery(String query, String directorAndTitle) {
        String[] requestString = directorAndTitle.split(",");
        switch (requestString.length) {
            case 1:
                return requestString[0].equals("title") ? filmStorage.getFilmsByPartOfTitle(query)
                        : filmStorage.getFilmsByPartOfDirectorName(query);
            case 2:
                List<Film> filmsWithSearchedNames = filmStorage.getFilmsByPartOfTitle(query);
                List<Film> filmsWithSearchedDirectors = filmStorage.getFilmsByPartOfDirectorName(query);
                filmsWithSearchedDirectors.addAll(filmsWithSearchedNames);
                return filmsWithSearchedDirectors;
            default:
                return Collections.emptyList();
        }
    }

    public List<Film> searchFilms(String query, String directorAndTitle) {
        return (query == null && directorAndTitle == null) ? findFilteredPopularFilms()
                : searchFilmsByQuery(query, directorAndTitle).stream()
                .sorted(Comparator.comparing(Film::getRate).thenComparing(Film::getId).reversed())
                .collect(Collectors.toList());
    }

    public Film getById(long id) {
        log.info("Get film with id=" + id);
        return filmStorage.get(id)
                .orElseThrow(() -> new NotExistException("Film with id=" + id + " not exists"));
    }

    public Film update(Film film) {
        if (film == null) {
            throw new NotExistException("Передан пустой аргумент!");
        }
        getById(film.getId());

        var genres = Optional.ofNullable(film.getGenres()).stream()
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
        film.setGenres(genres);
        genreService.update(film.getId(), genres);
        directorService.updateAllToFilm(film.getId(), film.getDirectors());

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
