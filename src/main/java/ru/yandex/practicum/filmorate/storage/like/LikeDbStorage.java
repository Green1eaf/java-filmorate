package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Long> getAll(long id) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id);
    }

    @Override
    public void add(Long filmId, Long userId, Long mark) {
        jdbcTemplate.update("INSERT INTO likes(film_id, user_id, mark) VALUES (?,?, ?)", filmId, userId, mark);
    }

    @Override
    public void remove(long userId, long filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id=? AND film_id=?", userId, filmId);
    }
    @Override
    public List<Long> getRecommendations(long userId) {
        return jdbcTemplate.queryForList(
                "SELECT lf.film_id "
                        + "FROM likes AS lf "
                        + "WHERE lf.user_id IN "
                        + "(SELECT l2.user_id "
                        + "FROM LIKES AS l "
                        + "JOIN LIKES AS l2 ON l2.user_id != l.user_id "
                        + "AND l.film_id = l2.film_id AND l.mark = l2.mark "
                        + "WHERE l.user_id =? "
                        + "GROUP BY l2.user_id "
                        + "ORDER BY COUNT(l2.user_id) DESC "
                        + "LIMIT 1) "
                        + "AND lf.mark > 5", Long.class, userId);
    }
}
