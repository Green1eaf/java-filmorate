package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void add(long userId, long filmId) {
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES (?,?)", userId, filmId);
    }

    @Override
    public void delete(long userId, long filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", filmId, userId);
    }

    @Override
    public List<Long> getAll(long filmId) {
        return jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id=?",
                new Object[]{filmId}, new BeanPropertyRowMapper<>());
    }
}
