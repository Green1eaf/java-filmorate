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
    public List<Long> getAll(long filmId) {
        return jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id=?",
                new Object[]{filmId}, new BeanPropertyRowMapper<>());
    }

    @Override
    public void add(long userId, long filmId) {
        jdbcTemplate.update("INSERT INTO likes VALUES (?,?)", userId, filmId);
    }

    @Override
    public void remove(long userId, long filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id=? AND film_id=?", userId, filmId);
    }
}
