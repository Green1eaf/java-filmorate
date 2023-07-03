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

    public List<Long> getRecommendations(long userId) {
        return jdbcTemplate.queryForList(
                "SELECT lf.FILM_ID\n" +
                        "FROM likes AS lf\n" +
                        "WHERE lf.user_id IN\n" +
                        "(SELECT l2.USER_ID\n" +
                        "FROM LIKES AS l\n" +
                        "JOIN LIKES AS l2 ON l2.USER_ID != l.USER_ID " +
                        "AND l.FILM_ID = l2.FILM_ID AND l.mark = l2.mark\n" +
                        "WHERE l.USER_ID = ?\n" +
                        "GROUP BY l2.USER_ID\n" +
                        "ORDER BY COUNT(l2.USER_ID) desc\n" +
                        "LIMIT 1)\n" +
                        "AND lf.mark > 5;",
                Long.class, userId);
    }
}
