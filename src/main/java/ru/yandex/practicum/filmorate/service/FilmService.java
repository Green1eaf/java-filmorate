package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private long counter = 1;
    private static final int DEFAULT_NUMBER_OF_POPULAR_FILMS = 10;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addFilm(Film film) {
        if (film.getId() == null) {
            film.setId(counter++);
        } else {
            //TODO обработать исключение
        }
        filmStorage.add(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.findAll().stream()
                .filter(f -> f.getId().equals(film.getId()))
                .findFirst()
                .map(f -> {
                    filmStorage.delete(f.getId());
                    filmStorage.add(film);
                    log.info("updated film with id: {} and name: {}", film.getId(), film.getName());
                    return film;
                })
                .orElseThrow(() -> {
                    log.info("validation failed for film name: {}", film.getName());
                    return new ValidationException();
                });
    }

    public void like(long id, long userId) {
        filmStorage.get(id).addLike(userId);
        log.info("like for film with id={} from user with id={}", id, userId);
    }

    public void removeLike(long id, long userId) {
        filmStorage.get(id).removeLike(userId);
        log.info("remove like from film with id={}, from user with id={}", id, userId);
    }

    public List<Film> findCertainNumberPopularFilms(Integer count) {
        List<Film> films = filmStorage.findAll();
        return films.stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count == null ? DEFAULT_NUMBER_OF_POPULAR_FILMS : count)
                .collect(Collectors.toList());
    }
}
