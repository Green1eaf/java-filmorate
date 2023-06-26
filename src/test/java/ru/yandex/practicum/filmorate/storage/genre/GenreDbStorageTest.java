package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GenreDbStorageTest {

    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;
    private Film film;

    @BeforeEach
    public void init() {
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(Collections.emptyList())
                .build();
    }


    @Test
    void add() {
        film = filmStorage.create(film);
        genreStorage.add(film.getId(), List.of(new Genre(1L, "Комедия")));
        film = filmStorage.get(film.getId()).orElse(null);
        assertArrayEquals(List.of(new Genre(1L, "Комедия")).toArray(),
                film.getGenres().toArray());
    }

    @Test
    void get() {
        assertEquals(Optional.of(new Genre(1L, "Комедия")), genreStorage.get(1L));
    }

    @Test
    void getAll() {
        assertArrayEquals(List.of(
                new Genre(1L, "Комедия"),
                new Genre(2L, "Драма"),
                new Genre(3L, "Мультфильм"),
                new Genre(4L, "Триллер"),
                new Genre(5L, "Документальный"),
                new Genre(6L, "Боевик")
        ).toArray(), genreStorage.getAll().toArray());
    }
}