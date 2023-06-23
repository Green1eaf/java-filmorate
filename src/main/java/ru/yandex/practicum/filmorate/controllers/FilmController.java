package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private static final String DEFAULT_SEARCH_LIMIT_VALUE = "10";


    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilmsWithFilter(@RequestParam(required = false, name = "count", defaultValue = DEFAULT_SEARCH_LIMIT_VALUE) Integer limit,
                                                 @RequestParam(required = false, name = "genreId") Long filterByGenreId,
                                                 @RequestParam(required = false, name = "year") Integer filterByYear) {
        return filmService.findFilteredPopularFilms(limit, filterByGenreId, filterByYear);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.getById(id);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam(name = "userId") long userId,
                                     @RequestParam(name = "friendId") long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void removeById(@PathVariable long filmId) {
        filmService.removeById(filmId);
    }

    @GetMapping("/director/{id}")
    public List<Film> getFilmsByDirector(@PathVariable long id, @RequestParam(defaultValue = "id") String sortBy) {
        return filmService.getFilmsByDirector(id, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam(required = false, name = "query") String searchQuery,
                                  @RequestParam(required = false, name = "by") String searchParams) {
        return filmService.searchFilms(searchQuery, searchParams);
    }

}
