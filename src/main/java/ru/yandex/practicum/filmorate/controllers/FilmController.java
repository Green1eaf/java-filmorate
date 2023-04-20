package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        try {
            repository.stream()
                    .filter(f -> f.getId().equals(film.getId()))
                    .findFirst()
                    .orElseThrow(ValidationException::new);
        } catch (ValidationException e) {
            log.info("validation failed for film name: {}", film.getName());
            throw new ValidationException();
        }
        repository.removeIf(f -> f.getId().equals(film.getId()));
        repository.add(film);
        log.info("updated film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }
}
