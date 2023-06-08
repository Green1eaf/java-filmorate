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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreDbStorageTest {

    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;
    private Film FILM;

    @BeforeEach
    public void init() {
        FILM = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .likes(Collections.emptySet())
                .genres(Collections.emptyList())
                .build();
    }


    @Test
    void add() {
        FILM = filmStorage.create(FILM);
        genreStorage.add(FILM.getId(), List.of(new Genre(1L, "Комедия")));
        FILM = filmStorage.get(FILM.getId());
        assertArrayEquals(List.of(new Genre(1L, "Комедия")).toArray(),
                FILM.getGenres().toArray());
    }

    @Test
    void get() {
        assertEquals(new Genre(1L, "Комедия"), genreStorage.get(1L));
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