package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public void add(Genre genre) {
        genreStorage.add(genre);
    }

    public void update(Genre genre) {
        genreStorage.update(genre);
    }

    public Genre get(long id) {
        return genreStorage.get(id);
    }

    public void delete(long id) {
        genreStorage.delete(id);
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}
