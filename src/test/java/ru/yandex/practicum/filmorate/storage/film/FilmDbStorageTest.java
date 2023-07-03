package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeService likeService;
    private final DirectorStorage directorStorage;
    private static final Film FILM;
    private static final Film SECOND_FILM;
    private static final User USER;
    private static final User FRIEND;

    private static final Director DIRECTOR;

    static {
        USER = new User(null, "email@ya.com", "login", "name",
                LocalDate.of(1986, 3, 14), null);
        FRIEND = new User(null, "friend@ya.com", "friend", "friend",
                LocalDate.of(1986, 3, 14), null);
        FILM = new Film(null, "testfilm", "desc", LocalDate.of(2000, 1, 1), 100,
                new Mpa(1L, "G"), 0, Collections.emptyList(), List.of());
        SECOND_FILM = new Film(null, "film", "desc", LocalDate.of(2000, 1, 1), 100,
                new Mpa(1L, "G"), 0, Collections.emptyList(), List.of());
        DIRECTOR = new Director(1L, "Director");
    }

    @BeforeEach
    public void init() {
        FILM.setId(1L);
        SECOND_FILM.setId(2L);
    }

    @Test
    void create() {
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
    }

    @Test
    void delete() {
        filmStorage.delete(FILM.getId());
    }

    @Test
    void get() {
    }

    @Test
    void findAll() {
    }

    @Test
    void commonNoCommonFilmsBefore() {
        createUserAndFriend();
    }

    @Test
    void getCommonFilms() {
        addLikesAndGetCommonFilms();
    }

    @Test
    void getNoCommonFilmsAfterDeleteLikeFromUser() {
        addLikesAndGetCommonFilms();
    }

    @Test
    void getFilmsByDirector() {
    }

    private void createUserAndFriend() {
        userStorage.create(USER);
        userStorage.create(FRIEND);
    }

    private void addLikesAndGetCommonFilms() {
        createUserAndFriend();
    }

    @Test
    void getFilmByPartOfTitle() {
    }

    @Test
    void getFilmByPartOfDirectorName() {
    }
}