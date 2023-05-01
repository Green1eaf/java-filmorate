package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> repository = new ArrayList<>();
    private int counter = 1;

    @GetMapping
    public List<Film> findAllFilms() {
        return repository;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(counter++);
        }
        repository.add(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return repository.stream()
                .filter(f -> f.getId().equals(film.getId()))
                .findFirst()
                .map(f -> {
                    repository.remove(f);
                    repository.add(film);
                    log.info("updated film with id: {} and name: {}", film.getId(), film.getName());
                    return film;
                })
                .orElseThrow(() -> {
                    log.info("validation failed for film name: {}", film.getName());
                    return new ValidationException();
                });
    }
}
