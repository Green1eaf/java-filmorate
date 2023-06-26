package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }


    public Director getById(long id) {
        return directorStorage.get(id)
                .orElseThrow(() -> new NotExistException("Director is not found"));
    }

    public Director update(Director director) {
        getById(director.getId());
        return directorStorage.update(director);
    }

    public void remove(long id) {
        getById(id);
        directorStorage.delete(id);
    }

    public List<Director> getAll() {
        return directorStorage.findAll();
    }

    public void updateAllToFilm(long filmId, List<Director> directors) {
        directorStorage.updateAllToFilm(filmId, directors);
    }

    public void addAllToFilm(long filmId, List<Director> directors) {
        directorStorage.addAllToFilm(filmId, directors);
    }

    public List<Director> getAllByFilmId(long filmId) {
        return directorStorage.getAllByFilmId(filmId);
    }
}
