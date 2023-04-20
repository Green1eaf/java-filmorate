package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
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
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        repository.stream()
                .filter(f -> f.getId().equals(film.getId()))
                .findFirst()
                .orElseThrow(ValidationException::new);
        repository.removeIf(f -> f.getId().equals(film.getId()));
        repository.add(film);
        return film;
    }
}
