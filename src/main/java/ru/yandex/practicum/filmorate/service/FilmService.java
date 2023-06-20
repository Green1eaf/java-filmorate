package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private static final Integer DEFAULT_SEARCH_LIMIT_VALUE = 10;

    public FilmService(FilmStorage filmStorage, UserService userService, LikeStorage likeStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> findAll() {
        log.info("Find all films");
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (film.getId() != null) {
            throw new AlreadyExistException("Film " + film.getName() + " with id=" + film.getId() + " is already exist");
        }
        filmStorage.create(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }

    public void like(long filmId, long userId) {
        userService.get(userId);
        getById(filmId);
        likeStorage.add(userId, filmId);
        log.info("like for film with id={} from user with id={}", filmId, userId);
    }

    public void removeLike(long id, long userId) {
        userService.get(userId);
        getById(id);
        likeStorage.remove(userId, id);
        log.info("remove like from film with id={}, from user with id={}", id, userId);
    }

    public List<Film> findFilteredPopularFilms(Integer count, Long genreId, Integer year) {
        log.info("find " + count + " most popular films with genreId = " + genreId + " and release year = " + year);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::getRate).thenComparing(Film::getId).reversed())
                .limit(count == null ? DEFAULT_SEARCH_LIMIT_VALUE : count)
                .filter(genreId == null ? film -> true : film -> film.getGenres().contains(genreStorage.get(genreId)))
                .filter(year == null ? film -> true : film -> film.getReleaseDate().getYear() == year)
                .collect(Collectors.toList());
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
}
