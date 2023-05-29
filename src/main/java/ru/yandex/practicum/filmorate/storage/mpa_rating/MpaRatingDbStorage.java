package ru.yandex.practicum.filmorate.storage.mpa_rating;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Repository
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(MpaRating mpaRating) {
        jdbcTemplate.update("INSERT INTO mpa_ratings (id, name) VALUES (?,?)", mpaRating.getId(), mpaRating.getName());
    }

    @Override
    public void update(MpaRating mpaRating) {
        jdbcTemplate.update("UPDATE mpa_ratings SET name=? WHERE id=?", mpaRating.getName(), mpaRating.getId());
    }

    @Override
    public MpaRating get(long id) {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(MpaRating.class))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM mpa_ratings WHERE id=?", id);
    }

    @Override
    public List<MpaRating> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings", new BeanPropertyRowMapper<>(MpaRating.class));
    }
}
