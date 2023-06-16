package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void add(long filmId, List<Genre> genres) {
        if (genres != null) {
            List<Object[]> batchArgs = new ArrayList<>();
            for (Genre genre : genres) {
                batchArgs.add(new Object[]{filmId, genre.getId()});
            }
            jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)", batchArgs);
        }
    }

    @Override
    @Transactional
    public void update(long filmId, List<Genre> genres) {
        delete(filmId);
        add(filmId, genres);
    }

    @Override
    public Genre get(long id) {
        return jdbcTemplate.query("SELECT * FROM genres WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Genre.class))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotExistException("Genre with id=" + id + " not exists"));
    }

    @Override
    @Transactional
    public void delete(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", filmId);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public List<Genre> getAllByFilmId(long id) {
        var genre = jdbcTemplate.query("SELECT * FROM genres g LEFT JOIN film_genre f ON g.id=f.genre_id " +
                        "WHERE f.film_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Genre.class));
        return CollectionUtils.isEmpty(genre) ? Collections.emptyList() : genre;
    }
}
