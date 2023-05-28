package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> storage = new HashMap<>();

    @Override
    public Film add(Film film) {
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return add(film);
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public Film get(long id) {
        return storage.get(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(storage.values());
    }
}
