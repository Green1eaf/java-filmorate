package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private Film film;

    @BeforeEach
    public void init() {
        film = Film.builder()
                .name("test")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(Collections.emptyList())
                .likes(Collections.emptySet())
                .build();
        filmStorage.create(film);
        film.setId(1L);
    }

    @Test
    void create() {
        assertEquals(film, filmStorage.get(film.getId()));
    }

    @Test
    void update() {
        var updatedFilm = Film.builder()
                .id(film.getId())
                .name("updated")
                .description("upDesc")
                .releaseDate(LocalDate.of(2001, 2, 2))
                .duration(110)
                .mpa(new Mpa(2L, "PG"))
                .build();
        assertEquals(updatedFilm, filmStorage.update(updatedFilm));
    }

    @Test
    void delete() {
        film.setMpa(new Mpa(1L, "G"));
        assertEquals(film, filmStorage.get(film.getId()));
        filmStorage.delete(film.getId());
        assertThrows(NotExistException.class, () -> filmStorage.get(film.getId()));
    }

    @Test
    void get() {
        assertEquals(film, filmStorage.get(film.getId()));
    }

    @Test
    void findAll() {
        assertArrayEquals(List.of(film).toArray(), filmStorage.findAll().toArray());
    }
}