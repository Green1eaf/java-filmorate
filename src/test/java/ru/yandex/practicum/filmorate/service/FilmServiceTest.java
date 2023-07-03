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

        secondFilm = Film.builder()
                .name("name2")
                .description("desc2")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(List.of(new Genre(1L, "testGenre")))
                .directors(List.of())
                .build();

        thirdFilm = Film.builder()
                .name("name3")
                .description("desc3")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(2L, "PG"))
                .genres(List.of(new Genre(2L, "testGenre2")))
                .directors(List.of())
                .build();

        User user = User.builder()
                .email("test@ya.com")
                .login("testLogin")
                .name("updateName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();

        User friend = User.builder()
                .email("friend@ya.com")
                .login("friendLogin")
                .name("friendName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();

        director = Director.builder()
                .name("Director name")
                .build();
    }

    @Test
    public void findAllFilms() {
    }

    @Test
    public void findTopPopularFilms() {
    }

    @Test
    public void searchFilm() {
    }
}
