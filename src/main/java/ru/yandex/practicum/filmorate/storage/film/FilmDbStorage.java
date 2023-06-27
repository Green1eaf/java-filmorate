package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) " +
                            "VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId((Objects.requireNonNull(keyHolder.getKey()).longValue()));
        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films " +
                        "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? " +
                        "WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;

    }

    @Override
    @Transactional
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id=?", id);
    }

    @Override
    public Optional<Film> get(long id) {
        return jdbcTemplate.query(
                        "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                                + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                                + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                                + "GROUP_CONCAT(g.name) AS genresnames, "
                                + "GROUP_CONCAT(d.id) AS directorsid, "
                                + "GROUP_CONCAT(d.NAME) AS directorsname "
                                + "FROM films AS f\n"
                                + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                                + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                                + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                                + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                                + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                                + "WHERE f.id = ? "
                                + "GROUP BY f.id "
                                + "ORDER BY COUNT(l.user_id)",
                        new Object[]{id}, new FilmMapper()).stream()
                .findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                        + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                        + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                        + "GROUP_CONCAT(g.name) AS genresnames, "
                        + "GROUP_CONCAT(d.id) AS directorsid, "
                        + "GROUP_CONCAT(d.NAME) AS directorsname "
                        + "FROM films AS f "
                        + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                        + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                        + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                        + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                        + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                        + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                        + "GROUP BY f.id",
                new FilmMapper());
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                        + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                        + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                        + "GROUP_CONCAT(g.name) AS genresnames, "
                        + "GROUP_CONCAT(d.id) AS directorsid, "
                        + "GROUP_CONCAT(d.NAME) AS directorsname "
                        + "FROM films AS f "
                        + "INNER JOIN likes l1 ON f.id = l1.film_id AND l1.user_id=? "
                        + "INNER JOIN likes l2 ON f.id = l2.film_id AND l2.user_id=? "
                        + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                        + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                        + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                        + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                        + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                        + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                        + "GROUP BY f.id "
                        + "ORDER BY likes DESC",
                new Object[]{userId, friendId}, new FilmMapper());
    }

    @Override
    public List<Film> getFilmsByDirector(long id, String sortBy) {
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date as years, f.duration, "
                        + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                        + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                        + "GROUP_CONCAT(g.name) AS genresnames, "
                        + "GROUP_CONCAT(d.id) AS directorsid, "
                        + "GROUP_CONCAT(d.NAME) AS directorsname "
                        + "FROM films AS f "
                        + "LEFT OUTER JOIN likes l1 ON f.id = l1.film_id "
                        + "LEFT OUTER JOIN likes l2 ON f.id = l2.film_id "
                        + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                        + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                        + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                        + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                        + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                        + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                        + "WHERE d.id = ? "
                        + "GROUP BY f.id "
                        + "ORDER BY " + sortBy,
                new Object[]{id}, new FilmMapper());
    }

    @Override
    public List<Film> getFilmsByPartOfTitle(String filmNamePart) {
        String sqlString = "SELECT f.id, f.name, f.description, f.release_date as years, f.duration, "
                + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                + "GROUP_CONCAT(g.name) AS genresnames, "
                + "GROUP_CONCAT(d.id) AS directorsid, "
                + "GROUP_CONCAT(d.NAME) AS directorsname "
                + "FROM films AS f "
                + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                + "WHERE LCASE(f.NAME) LIKE '%" + filmNamePart.toLowerCase() + "%' "
                + "GROUP BY f.id ";
        return jdbcTemplate.query(sqlString, new FilmMapper());

    }

    public List<Film> getFilmsByPartOfDirectorName(String directorNamePart) {
        String sqlString = "SELECT f.id, f.name, f.description, f.release_date as years, f.duration, "
                + "f.mpa_rating_id, m.name AS mpa_name, avg(l.MARK) AS likes, "
                + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                + "GROUP_CONCAT(g.name) AS genresnames, "
                + "GROUP_CONCAT(DISTINCT d.id) AS directorsid, "
                + "GROUP_CONCAT(d.NAME) AS directorsname "
                + "FROM films AS f "
                + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                + "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.id "
                + "LEFT OUTER JOIN directors AS d ON d.id = fd.director_ID "
                + "WHERE LCASE(d.NAME) LIKE '%" + directorNamePart.toLowerCase() + "%' "
                + "GROUP BY f.id ";
        return jdbcTemplate.query(sqlString, new FilmMapper());
    }
}
