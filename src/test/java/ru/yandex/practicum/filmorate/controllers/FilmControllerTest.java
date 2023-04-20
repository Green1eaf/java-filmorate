package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    private Film film;

    @BeforeEach
    public void init() {
        film = new Film(1, "Matrix", "About Matrix",
                LocalDate.of(2000, 10, 10), 100);
    }


    @Test
    void findAllFilms() {
        filmController.addFilm(film);
        Assertions.assertEquals(2, filmController.findAllFilms().size());

    }

    @Test
    void addFilm() {
        Assertions.assertEquals(0, filmController.findAllFilms().size());
        Film testFilm = filmController.addFilm(film);
        Assertions.assertArrayEquals(List.of(testFilm).toArray(), filmController.findAllFilms().toArray());
        Assertions.assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void updateFilm() throws ValidationException {
        Film testFilm = filmController.addFilm(film);
        testFilm.setName("UpdateMarix");
        filmController.updateFilm(testFilm);
        Assertions.assertArrayEquals(List.of(testFilm).toArray(), filmController.findAllFilms().toArray());
    }
}