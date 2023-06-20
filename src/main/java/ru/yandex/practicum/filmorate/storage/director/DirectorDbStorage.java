package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sqlQuery = "SELECT * FROM DIRECTORS";
        List<Optional<Director>> directors = jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
        List<Director> result = new ArrayList<>();
        for (Optional<Director> director : directors) {
            director.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(DirectorMapper.toMap(director)).longValue();
        director.setId((int) id);
        return director;
    }

    @Override
    public Optional<Director> get(long id) {
        try {
            String sqlQuery = "SELECT ID, NAME FROM genres where id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "update directors set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from directors where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Optional<Director> mapRowToDirector(ResultSet resultSet, int i) throws SQLException {
        return Optional.of(Director.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build());
    }
}
