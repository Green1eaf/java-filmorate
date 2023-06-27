package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void add(long filmId, List<Genre> genres) {
        jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = genres.get(i);
                        ps.setLong(1, filmId);
                        ps.setLong(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    @Transactional
    public void update(long filmId, List<Genre> genres) {
        delete(filmId);
        add(filmId, genres);
    }

    @Override
    public Optional<Genre> get(long id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
    }

    @Override
    @Transactional
    public void delete(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", filmId);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", new BeanPropertyRowMapper<>(Genre.class));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
