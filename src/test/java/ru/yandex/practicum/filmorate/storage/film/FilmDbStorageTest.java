package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final DirectorStorage directorStorage;
    private static final Film FILM;
    private static final Film SECONDFILM;
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
        SECONDFILM = new Film(null, "film", "desc", LocalDate.of(2000, 1, 1), 100,
                new Mpa(1L, "G"), 0, Collections.emptyList(), List.of());
        DIRECTOR = new Director(1L, "Director");
    }

    @BeforeEach
    public void init() {
        filmStorage.create(FILM);
        filmStorage.create(SECONDFILM);
        FILM.setId(1L);
        SECONDFILM.setId(2L);
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
        assertEquals(FILM, filmStorage.get(FILM.getId()));
        filmStorage.delete(FILM.getId());
        assertNull(filmStorage.get(FILM.getId()));
    }

    @Test
    void get() {
        assertEquals(FILM, filmStorage.get(FILM.getId()));
    }

    @Test
    void findAll() {
        assertArrayEquals(List.of(FILM,SECONDFILM).toArray(), filmStorage.findAll().toArray());
    }

    @Test
    void commonNoCommonFilmsBefore() {
        createUserAndFriend();
        Assertions.assertArrayEquals(filmStorage.getCommonFilms(USER.getId(), FRIEND.getId()).toArray(),
                Collections.emptyList().toArray());
    }

    @Test
    void getCommonFilms() {
        addLikesAndGetCommonFilms();
    }

    @Test
    void getNoCommonFilmsAfterDeleteLikeFromUser() {
        addLikesAndGetCommonFilms();
        likeStorage.remove(USER.getId(), FILM.getId());
        Assertions.assertArrayEquals(filmStorage.getCommonFilms(USER.getId(), FRIEND.getId()).toArray(),
                Collections.emptyList().toArray());
    }

    @Test
    void getFilmsByDirector() {
        directorStorage.create(DIRECTOR);
        directorStorage.addAllToFilm(1L, List.of(DIRECTOR));
        Assertions.assertEquals(1, filmStorage.getFilmsByDirector(1L, "likes").toArray().length);
    }

    private void createUserAndFriend() {
        userStorage.create(USER);
        userStorage.create(FRIEND);
    }

    private void addLikesAndGetCommonFilms() {
        createUserAndFriend();
        likeStorage.add(USER.getId(), FILM.getId());
        likeStorage.add(FRIEND.getId(), FILM.getId());
        FILM.setRate(2);
        Assertions.assertArrayEquals(filmStorage.getCommonFilms(USER.getId(), FRIEND.getId()).toArray(),
                List.of(FILM).toArray());
    }

    @Test
    void getFilmByPartOfTitle() {
        assertEquals(filmStorage.get(FILM.getId()), filmStorage.getFilmsByPartOfTitle("te").get(0));
    }

    @Test
    void getFilmByPartOfDirectorName() {
        directorStorage.create(DIRECTOR);
        directorStorage.addAllToFilm(FILM.getId(), List.of(DIRECTOR));
        assertEquals(filmStorage.get(FILM.getId()), filmStorage.getFilmsByPartOfDirectorName("dir").get(0));
    }

    @Test
    void searchFilm() {
        directorStorage.create(DIRECTOR);
        directorStorage.addAllToFilm(FILM.getId(), List.of(DIRECTOR));
        directorStorage.addAllToFilm(SECONDFILM.getId(), List.of(DIRECTOR));
        assertEquals(filmStorage.findAll(), filmStorage.searchFilms("film", "title"));
        assertEquals(filmStorage.findAll(), filmStorage.searchFilms("dir", "director"));
        assertEquals(filmStorage.findAll(), filmStorage.searchFilms("film", "title,director"));
    }
}