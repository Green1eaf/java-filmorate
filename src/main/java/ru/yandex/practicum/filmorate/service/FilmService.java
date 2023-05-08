package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private int counter = 1;

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
}
