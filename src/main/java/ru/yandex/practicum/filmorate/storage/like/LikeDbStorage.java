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
    public List<Long> getRecommendations (long userId){
        //TODO сделать рефакторинг запроса (ключевые слова запроса прописными буквами)
        return jdbcTemplate.queryForList(  "select lf.FILM_ID\n" +
                "from likes as lf\n" +
                "where lf.user_id in\n" +
                "      (select l2.USER_ID\n" +
                "       from LIKES as l\n" +
                "                join LIKES as l2 on l2.USER_ID != l.USER_ID and l.FILM_ID = l2.FILM_ID and l.mark = l2.mark\n" +
                "       where l.USER_ID = ?\n" +
                "       group by l2.USER_ID\n" +
                "       order by count(l2.USER_ID) desc\n" +
                "       limit 1)\n" +
                "  and lf.mark > 5;", Long.class, userId);
    }
}
