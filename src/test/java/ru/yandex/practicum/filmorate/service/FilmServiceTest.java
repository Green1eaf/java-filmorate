package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmServiceTest {

    private final FilmService filmService;
    private final UserService userService;
    private final DirectorService directorService;
    private Film firstFilm;
    private Film secondFilm;
    private Film thirdFilm;
    private User user;
    private User friend;
    private Director director;

    @BeforeEach
    public void init() {
        firstFilm = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(List.of(new Genre(1L, "testGenre")))
                .directors(List.of())
                .build();
        filmService.create(firstFilm);

        secondFilm = Film.builder()
                .name("name2")
                .description("desc2")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(List.of(new Genre(1L, "testGenre")))
                .directors(List.of())
                .build();
        filmService.create(secondFilm);

        thirdFilm = Film.builder()
                .name("name3")
                .description("desc3")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(2L, "PG"))
                .genres(List.of(new Genre(2L, "testGenre2")))
                .directors(List.of())
                .build();
        filmService.create(thirdFilm);


        user = User.builder()
                .email("test@ya.com")
                .login("testLogin")
                .name("updateName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();
        userService.create(user);

        friend = User.builder()
                .email("friend@ya.com")
                .login("friendLogin")
                .name("friendName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();
        userService.create(friend);

        director = Director.builder()
                .name("Director name")
                .build();
    }

    @Test
    public void findAllFilms() {
        assertEquals(3, filmService.findAll().size());
    }

    @Test
    public void findTopPopularFilms() {
        assertEquals(1, filmService.findFilteredPopularFilms(2, 1L, 2001).size());
        assertEquals(2, filmService.findFilteredPopularFilms(10, 1L, null).size());
        assertEquals(2, filmService.findFilteredPopularFilms(10, null, 2001).size());
    }

    @Test
    public void searchFilm() {
        directorService.create(director);
        directorService.addAllToFilm(firstFilm.getId(), List.of(director));
        directorService.addAllToFilm(secondFilm.getId(), List.of(director));
        directorService.addAllToFilm(thirdFilm.getId(), List.of(director));
        assertEquals(3, filmService.searchFilms("name", "title").size());
        assertEquals(3, filmService.searchFilms("dir", "director").size());
        assertEquals(6, filmService.searchFilms("name", "title,director").size());
    }
}


