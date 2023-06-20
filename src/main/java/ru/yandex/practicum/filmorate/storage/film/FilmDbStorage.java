package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreService genreService;
    private final LikeStorage likeStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaRatingStorage mpaRatingService, GenreService genreService, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingStorage = mpaRatingService;
        this.genreService = genreService;
        this.likeStorage = likeStorage;
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
        genreService.addAll(film.getId(), film.getGenres());

        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        if (film == null) {
            throw new NotExistException("Передан пустой аргумент!");
        }
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa_rating_id = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            var genres = Optional.ofNullable(film.getGenres()).stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
            film.setGenres(genres);
            genreService.update(film.getId(), genres);
            return film;
        } else {
            throw new NotExistException("Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id=?", id);
    }

    @Override
    public Film get(long id) {
        return jdbcTemplate.query(
                        "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                                + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                                + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                                + "GROUP_CONCAT(g.name) AS genresnames "
                                + "FROM films AS f\n"
                                + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                                + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                                + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                                + "WHERE f.id = ? "
                                + "GROUP BY f.id "
                                + "ORDER BY COUNT(l.user_id)",
                        new Object[]{id}, new FilmMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                        + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                        + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                        + "GROUP_CONCAT(g.name) AS genresnames "
                        + "FROM films AS f "
                        + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                        + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                        + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                        + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                        + "LEFT OUTER JOIN film_director AS d ON d.film_id = f.id "
                        + "GROUP BY f.id",
                new FilmMapper());
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, "
                        + "f.mpa_rating_id, m.name AS mpa_name, COUNT(l.user_id) AS likes, "
                        + "GROUP_CONCAT(DISTINCT fg.genre_id) AS genresid, "
                        + "GROUP_CONCAT(g.name) AS genresnames "
                        + "FROM films AS f "
                        + "INNER JOIN likes l1 ON f.id = l1.film_id AND l1.user_id=? "
                        + "INNER JOIN likes l2 ON f.id = l2.film_id AND l2.user_id=? "
                        + "JOIN mpa_ratings AS m ON m.id = f.mpa_rating_id "
                        + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                        + "LEFT OUTER JOIN film_genre AS fg ON fg.film_id = f.id "
                        + "LEFT OUTER JOIN genres AS g ON g.id = fg.genre_id "
                        + "GROUP BY f.id "
                        + "ORDER BY likes DESC",
                new Object[]{userId, friendId}, new FilmMapper());
    }
}
