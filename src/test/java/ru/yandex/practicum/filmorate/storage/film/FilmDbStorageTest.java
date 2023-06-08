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
@Sql(scripts = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private Film FILM;

    @BeforeEach
    public void init() {
        FILM = Film.builder()
                .name("test")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(Collections.emptyList())
                .likes(Collections.emptySet())
                .build();
        filmStorage.create(FILM);
        FILM.setId(1L);
    }

    @Test
    void create() {
        assertEquals(FILM, filmStorage.get(FILM.getId()));
    }

    @Test
    void update() {
        var updatedFilm = Film.builder()
                .id(FILM.getId())
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
        FILM.setMpa(new Mpa(1L, "G"));
        assertEquals(FILM, filmStorage.get(FILM.getId()));
        filmStorage.delete(FILM.getId());
        assertThrows(NotExistException.class, () -> filmStorage.get(FILM.getId()));
    }

    @Test
    void get() {
        assertEquals(FILM, filmStorage.get(FILM.getId()));
    }

    @Test
    void findAll() {
        assertArrayEquals(List.of(FILM).toArray(), filmStorage.findAll().toArray());
    }
}