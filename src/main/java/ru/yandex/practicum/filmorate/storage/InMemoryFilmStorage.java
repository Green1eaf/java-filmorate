package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> storage = new HashMap<>();
    private static int count = 1;

    @Override
    public Film add(Film film) {
        if (film.getId() == null) {
            film.setId(count++);
        }
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public Film get(int id) {
        return storage.get(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(storage.values());
    }
}
