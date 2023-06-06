package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (film.getId() != null) {
            throw new AlreadyExistException("Film " + film.getName() + " with id=" + film.getId() + " is already exist");
        }
        filmStorage.add(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }

    public Film updateFilm(Film film) {
        get(film.getId());
        return filmStorage.update(film);
//        return filmStorage.findAll().stream()
//                .filter(f -> f.getId().equals(film.getId()))
//                .findFirst()
//                .map(f -> {
//                    filmStorage.update(film);
//                    log.info("updated film with id: {} and name: {}", film.getId(), film.getName());
//                    return film;
//                })
//                .orElseThrow(() -> new ValidationException("validation failed for film name: " + film.getName()));
    }

    public void like(long filmId, long userId) {
        userService.get(userId);
        var film = get(filmId);

        if (film.getLikes().contains(userId)) {
            throw new AlreadyExistException("Like from user with id=" + userId + " is already exist");
        }

        filmStorage.get(filmId).getLikes().add(userId);
        log.info("like for film with id={} from user with id={}", filmId, userId);
    }

    public void removeLike(long id, long userId) {
        userService.get(userId);
        get(id).getLikes().remove(userId);
        log.info("remove like from film with id={}, from user with id={}", id, userId);
    }

    public List<Film> findCertainNumberPopularFilms(Integer count) {
        log.info("find " + count + " most popular films");
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).thenComparing(Film::getId).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    //TODO
    public Film get(long id) {
        return Optional.ofNullable(filmStorage.get(id))
                .orElseThrow(() -> new NotExistException("Film with id=" + id + " not exist"));
    }
}
