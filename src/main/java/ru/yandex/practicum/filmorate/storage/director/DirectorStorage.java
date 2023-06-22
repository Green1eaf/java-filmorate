package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findAll();

    Director create(Director director);

    Optional<Director> get(long id);

    Director update(Director director);

    void delete(long id);


    void deleteAllFromFilm(long filmId);

    List<Director> getAllByFilmId(long id);

    void addAllToFilm(long filmId, List<Director> directors);

    void updateAllToFilm(long filmId, List<Director> directors);
}
