package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;

    private final Film film = new Film(1, "Matrix", "About Matrix",
            LocalDate.of(2000, 10, 10), 100);
    private final User user = new User(1, "mail@mail.com", "mata", "Mata Hari",
            LocalDate.of(1986, 3, 14));

    @Test
    public void findAllFilms() {
        filmController.addFilm(film);
        Assertions.assertEquals(2, filmController.findAllFilms().size());

    }

    @Test
    public void addFilm() {
        Assertions.assertEquals(0, filmController.findAllFilms().size());
        Film testFilm = filmController.addFilm(film);
        Assertions.assertArrayEquals(List.of(testFilm).toArray(), filmController.findAllFilms().toArray());
        Assertions.assertEquals(1, filmController.findAllFilms().size());
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
        Assertions.assertThrows(ValidationException.class, () -> filmController.updateFilm(new Film(999, "name", "desc",
                LocalDate.of(2000, 1, 1), 100)));
    }

    @Test
    void findAllUsers() {
        Assertions.assertEquals(2, userController.findAllUsers().size());
    }

    @Test
    void createUser() {
        userController.createUser(user);
        Assertions.assertEquals(2, userController.findAllUsers().size());
    }

    @Test
    void updateUser() throws ValidationException {
        User testUser = userController.createUser(user);
        testUser.setName("UpdateName");
        userController.updateUser(testUser);
        Assertions.assertArrayEquals(List.of(testUser).toArray(), userController.findAllUsers().toArray());
    }

    @Test
    public void updateUnknownUser() {
        Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUser(new User(999, "name@mail.com", "login", "name",
                        LocalDate.of(2000, 1, 1))));
    }
}
