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
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;

    private final Film film = Film.builder()
            .id(null)
            .name("Matrix")
            .description("About Matrix")
            .releaseDate(LocalDate.of(2000, 10, 10))
            .duration(100)
            .build();
    private final User user = User.builder()
            .id(null)
            .email("mail@mail.com")
            .login("mata")
            .name("Mata Hari")
            .birthdate(LocalDate.of(1986, 3, 14))
            .build();

    @BeforeEach
    public void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage())));
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void addFilmAndFindAllFilms() {
        filmController.addFilm(film);
        Assertions.assertEquals(1L, filmController.findAllFilms().size());
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
        Assertions.assertThrows(NotExistException.class, () -> filmController.updateFilm(
                Film.builder()
                        .id(999L)
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 1, 1))
                        .duration(100)
                        .build()));
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
        Assertions.assertThrows(NotExistException.class,
                () -> userController.updateUser(User.builder()
                        .id(999L)
                        .email("name@mail.com")
                        .login("login")
                        .name("name")
                        .birthdate(LocalDate.of(2000, 1, 1))
                        .build()));
    }
}