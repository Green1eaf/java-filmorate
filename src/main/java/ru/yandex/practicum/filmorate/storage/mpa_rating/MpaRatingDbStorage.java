package ru.yandex.practicum.filmorate.storage.mpa_rating;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> get(long id) {
        return jdbcTemplate.queryForStream("SELECT * FROM mpa_ratings WHERE id=?",
                        (rs, rowNum) -> new Mpa(rs.getLong("id"), rs.getString("name")), id)
                .findFirst();
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings", new BeanPropertyRowMapper<>(Mpa.class));
    }
}
