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

    public void addAll(long filmId, List<Genre> genres) {
        genreStorage.add(filmId, genres);
    }

    public void update(long filmId, List<Genre> genres) {
        genreStorage.update(filmId, genres);
    }

    public Genre get(long id) {
        return genreStorage.get(id);
    }

    public void delete(long filmId) {
        genreStorage.delete(filmId);
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}
