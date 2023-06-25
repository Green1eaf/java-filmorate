package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sqlQuery = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(DirectorMapper.toMap(director)).longValue();
        director.setId((int)id);
        return director;
    }

    @Override
    public Optional<Director> get(long id) {
        String sqlQuery = "SELECT ID, NAME FROM directors where id = ?";
        List<Director> directors = jdbcTemplate.query(sqlQuery, this::mapRowToDirector, id);
        return directors.isEmpty() ? Optional.empty() : Optional.of(directors.get(0));
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "update directors set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from directors where id = ?", id);
    }

    @Override
    public void deleteAllFromFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", filmId);
    }

    @Override
    public List<Director> getAllByFilmId(long id) {
        String sqlQuery = "SELECT FD.director_id as id, d.name as name FROM FILM_DIRECTOR"
                + " as FD JOIN DIRECTORS as d on d.id = fd.director_id WHERE FD.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector, id);
    }


    @Override
    public void addAllToFilm(long filmId, List<Director> directors) {
        if (directors != null) {
            List<Object[]> batchArgs = directors.stream()
                    .map(director -> new Object[]{filmId, director.getId()})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate("INSERT INTO film_director (film_id, director_id) VALUES (?,?)", batchArgs);
        }
    }

    @Override
    public void updateAllToFilm(long filmId, List<Director> directors) {
        deleteAllFromFilm(filmId);
        addAllToFilm(filmId, directors);
    }

    private Director mapRowToDirector(ResultSet resultSet, int i) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
