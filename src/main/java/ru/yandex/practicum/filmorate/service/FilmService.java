package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private long counter = 1;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addFilm(Film film) {
        if (film.getId() == null) {
            film.setId(counter++);
        } else {
            throw new AlreadyExistException("Film " + film.getName() + " with id=" + film.getId() + " is already exist");
        }
        filmStorage.add(film);
        log.info("added film with id: {} and name: {}", film.getId(), film.getName());
        return film;
    }

    public Film updateFilm(Film film) {
        get(film.getId());
        return filmStorage.findAll().stream()
                .filter(f -> f.getId().equals(film.getId()))
                .findFirst()
                .map(f -> {
                    filmStorage.delete(f.getId());
                    filmStorage.add(film);
                    log.info("updated film with id: {} and name: {}", film.getId(), film.getName());
                    return film;
                })
                .orElseThrow(() -> new ValidationException("validation failed for film name: " + film.getName()));
    }

    public void like(long filmId, long userId) {
        userService.get(userId);
        var film = get(filmId);

        if (film.getLikes().contains(userId)) {
            throw new AlreadyExistException("Like from user with id=" + userId + " is already exist");
        }

        filmStorage.get(filmId).addLike(userId);
        log.info("like for film with id={} from user with id={}", filmId, userId);
    }

    public void removeLike(long id, long userId) {
        userService.get(userId);
        get(id).removeLike(userId);
        log.info("remove like from film with id={}, from user with id={}", id, userId);
    }

    public List<Film> findCertainNumberPopularFilms(Integer count) {
        log.info("find " + count + " most popular films");
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film get(long id) {
        return Optional.ofNullable(filmStorage.get(id))
                .orElseThrow(() -> new NotExistException("Film with id=" + id + " not exist"));
    }
}
