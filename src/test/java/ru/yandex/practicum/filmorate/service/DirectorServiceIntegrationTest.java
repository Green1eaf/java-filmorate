package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DirectorServiceIntegrationTest {

    @Autowired
    private MpaRatingService mpaRatingService;
    @Autowired
    private DirectorService directorService;
    @Autowired
    private FilmService filmService;

    @Test
    public void testGetById() {
        // Given
        Director director = new Director();
        director.setName("Steven Spielberg");
        directorService.create(director);
        long id = director.getId();

        // When
        Director result = directorService.getById(id);

        // Then
        assertThat(result).isEqualTo(director);
    }

    @Test
    public void testGetAll() {
        // Given
        Director director1 = new Director();
        director1.setName("Steven Spielberg");
        directorService.create(director1);
        Director director2 = new Director();
        director2.setName("Quentin Tarantino");
        directorService.create(director2);

        // When
        List<Director> result = directorService.getAll();

        // Then
        assertThat(result).isEqualTo(List.of(director1, director2));
    }

    @Test
    public void testCreate() {
        // Given
        Director director = new Director();
        director.setName("Steven Spielberg");

        // When
        Director result = directorService.create(director);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Steven Spielberg");
    }

    @Test
    public void testUpdate() {
        // Given
        Director director = new Director();
        director.setName("Steven Spielberg");
        directorService.create(director);
        long id = director.getId();

        Director updatedDirector = new Director();
        updatedDirector.setId(id);
        updatedDirector.setName("Steven Spielberg (updated)");

        // When
        Director result = directorService.update(updatedDirector);

        // Then
        assertThat(result).isEqualTo(updatedDirector);
    }

    @Test
    public void testRemove() {
        // Given
        Director director = new Director();
        director.setName("Steven Spielberg");
        directorService.create(director);
        long id = director.getId();

        // When
        directorService.remove(id);

        // Then
        assertThatThrownBy(() -> directorService.getById(id))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    public void testGetAllByFilmId() {
        // Given
        Director director1 = new Director();
        director1.setName("Steven Spielberg");
        directorService.create(director1);
        Director director2 = new Director();
        director2.setName("Quentin Tarantino");
        directorService.create(director2);
        Mpa mpa = mpaRatingService.get(1L);
        Film film = new Film();
        film.setName("testName");
        film.setReleaseDate(LocalDate.now());
        film.setMpa(mpa);
        film.setGenres(List.of());
        film.setDirectors(List.of());
        film.setDuration(10);
        film.setRate(1.0);
        film.setDescription("description");
        filmService.create(film);

        long filmId = 1L;
        directorService.addAllToFilm(film.getId(), Arrays.asList(director1, director2));

        // When
        List<Director> result = directorService.getAllByFilmId(filmId);

        // Then
        assertThat(result).isEqualTo(List.of(director1, director2));
    }
}
