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
        Film film = jdbcTemplate.query("SELECT * FROM films WHERE id=?", new Object[]{id},
                        new FilmMapper()).stream()
                .findFirst()
                .orElse(null);
        if (film == null) {
            throw new NotExistException("film with id=" + id + " not exists");
        }

        film.setMpa(mpaRatingStorage.get(film.getMpa().getId()));
        film.setGenres(genreService.getAllByFilmId(id));
        film.setLikes(new HashSet<>(likeStorage.getAll(id)));
        return film;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM films", (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpaRatingStorage.get(rs.getLong("mpa_rating_id")),
                new HashSet<>(likeStorage.getAll(rs.getLong("id"))),
                genreService.getAllByFilmId(rs.getLong("id")),
                null //Временная заглушка
        ));
    }
}
