package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;
    private final FilmService filmService;
    public DirectorService(DirectorStorage directorStorage, FilmService filmService) {
        this.directorStorage = directorStorage;
        this.filmService = filmService;
    }

    public Optional<Director> getDirector(long id) {
        Optional<Director> foundDirector = directorStorage.get(id);
        if (foundDirector.isPresent()) {
            return foundDirector;
        } else {
            throw new NotExistException("Director is not found");
        }
    }

    public List<Director> getAll() {
        return directorStorage.findAll();
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        if (directorStorage.get(director.getId()).isEmpty()) {
            throw new NotExistException("Director is not found");
        }
        return directorStorage.update(director);
    }

    public void remove(long id) {
        if (getDirector(id).isEmpty()) {
            throw new NotExistException("Director is not found");
        }
        directorStorage.delete(id);
    }

    public void removeAllFromFilm(long filmId) {
        directorStorage.delete(filmId);
    }

    public void updateAllToFilm(long filmId, List<Director> directors) {
        filmService.get(filmId);
        directorStorage.updateAllToFilm(filmId, directors);
    }

    public void addAllToFilm(long filmId, List<Director> directors) {
        filmService.get(filmId);
        directorStorage.addAllToFilm(filmId, directors);
    }

    public List<Director> getAllByFilmId(long filmId) {
        filmService.get(filmId);
        return directorStorage.getAllByFilmId(filmId);
    }
}
