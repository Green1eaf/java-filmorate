package ru.yandex.practicum.filmorate.storage.mpa_rating;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void add(Mpa mpa) {
        jdbcTemplate.update("INSERT INTO mpa_ratings (id, name) VALUES (?,?)", mpa.getId(), mpa.getName());
    }

    @Override
    @Transactional
    public void update(Mpa mpa) {
        jdbcTemplate.update("UPDATE mpa_ratings SET name=? WHERE id=?", mpa.getName(), mpa.getId());
    }

    @Override
    public Mpa get(long id) {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings WHERE id=?", new Object[]{id},
                        (rs, rowNum) -> new Mpa(rs.getLong("id"), rs.getString("name"))).stream()
                .findFirst()
                .orElseThrow(() -> new NotExistException("Rating with id=" + id + " not exists"));
    }

    @Override
    @Transactional
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM mpa_ratings WHERE id=?", id);
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings", new BeanPropertyRowMapper<>(Mpa.class));
    }
}
