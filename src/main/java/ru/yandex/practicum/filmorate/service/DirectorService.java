package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
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
        if (directorStorage.get((long) director.getId()).isEmpty()) {
            throw new NotExistException("director is not found");
        }
        return directorStorage.update(director);
    }

    public void remove(long id) {
        directorStorage.delete(id);
    }
}
