package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void add(Genre genre) {
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?,?)", genre.getId(), genre.getName());
    }

    @Override
    public void update(Genre genre) {
        jdbcTemplate.update("UPDATE genres SET name=? WHERE id=?", genre.getName(), genre.getId());
    }

    @Override
    public Genre get(long id) {
        return jdbcTemplate.query("SELECT * FROM genres WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Genre.class))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM genres WHERE id=?", id);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", new BeanPropertyRowMapper<>(Genre.class));
    }
}
