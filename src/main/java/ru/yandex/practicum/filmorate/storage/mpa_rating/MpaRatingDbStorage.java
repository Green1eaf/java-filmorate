package ru.yandex.practicum.filmorate.storage.mpa_rating;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(long id) {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings WHERE id=?", new Object[]{id},
                        (rs, rowNum) -> new Mpa(rs.getLong("id"), rs.getString("name"))).stream()
                .findFirst()
                .orElseThrow(() -> new NotExistException("Rating with id=" + id + " not exists"));
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings", new BeanPropertyRowMapper<>(Mpa.class));
    }
}
