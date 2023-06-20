package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String genresIdString = rs.getString("genresid");
        String genresNamesString = rs.getString("genresnames");

        if (genresIdString != null && genresNamesString != null
                && !genresIdString.isEmpty() && !genresNamesString.isEmpty()) {
            String[] genresId = genresIdString.split(",");
            String[] genresNames = genresNamesString.split(",");

            IntStream.range(0, genresId.length)
                    .forEach(i -> genres.add(Genre.builder()
                            .id(Long.parseLong(genresId[i]))
                            .name(genresNames[i])
                            .build()));
        }

        List<Director> directors = new ArrayList<>();

        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getLong("mpa_rating_id"), rs.getString("mpa_name")))
                .rate(rs.getInt("likes"))
                .genres(genres)
                .directors(directors)
                .build();
    }
}
