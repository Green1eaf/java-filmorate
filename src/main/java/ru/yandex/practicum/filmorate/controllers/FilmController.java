package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> repository = new ArrayList<>();

    @GetMapping
    public List<Film> findAllFilms() {
        return repository;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        repository.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        repository.removeIf(f -> f.getId() == film.getId());
        repository.add(film);
        return film;
    }
}
