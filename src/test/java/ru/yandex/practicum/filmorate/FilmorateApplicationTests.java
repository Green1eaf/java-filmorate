package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;

    private final Film film = new Film(1L, "Matrix", "About Matrix",
            LocalDate.of(2000, 10, 10), 100);
    private final User user = new User(1L, "mail@mail.com", "mata", "Mata Hari",
            LocalDate.of(1986, 3, 14));

    @BeforeEach
    public void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void addFilmAndFindAllFilms() {
        filmController.addFilm(film);
        Assertions.assertEquals(1, filmController.findAllFilms().size());
        Assertions.assertArrayEquals(List.of(film).toArray(), filmController.findAllFilms().toArray());
    }

    @Test
    public void updateFilm() throws ValidationException {
        Film testFilm = filmController.addFilm(film);
        testFilm.setName("UpdateMatrix");
        filmController.updateFilm(testFilm);
        Assertions.assertArrayEquals(List.of(testFilm).toArray(), filmController.findAllFilms().toArray());
    }

    @Test
    public void updateUnknownFilm() {
        Assertions.assertThrows(ValidationException.class, () -> filmController.updateFilm(
                new Film(999L, "name", "desc",
                        LocalDate.of(2000, 1, 1), 100)));
    }

    @Test
    public void createUserAndFindAllUsers() {
        userController.createUser(user);
        Assertions.assertEquals(1, userController.findAllUsers().size());
        Assertions.assertArrayEquals(List.of(user).toArray(), userController.findAllUsers().toArray());
    }

    @Test
    public void updateUser() throws ValidationException {
        User testUser = userController.createUser(user);
        testUser.setName("UpdateName");
        userController.updateUser(testUser);
        Assertions.assertArrayEquals(List.of(testUser).toArray(), userController.findAllUsers().toArray());
    }

    @Test
    public void updateUnknownUser() {
        Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(new User(999L, "name@mail.com", "login", "name",
                        LocalDate.of(2000, 1, 1))));
    }
}
