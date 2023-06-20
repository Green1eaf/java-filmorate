package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReviewDbStorageTest {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Film film;
    private User user;
    private Review review;

    @BeforeEach
    public void init() {
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1L, "G"))
                .genres(Collections.emptyList())
                .build();
        filmStorage.create(film);

        user = User.builder()
                .email("test@ya.com")
                .login("testLogin")
                .name("updateName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();
        userStorage.create(user);

        review = Review.builder()
                .content("This film is soo bad.")
                .isPositive(false)
                .userId(user.getId())
                .filmId(film.getId())
                .build();
        reviewStorage.create(review);
    }

    @Test
    void create() {
        Review newReview = createNewReview();
        assertEquals(newReview, reviewStorage.get(newReview.getReviewId()).orElse(null));
    }

    @Test
    void update() {
        review.setContent("This film is not too bad");
        reviewStorage.update(review);
        Review testReview = reviewStorage.get(review.getReviewId()).orElse(null);
        assertEquals(review.getContent(), testReview.getContent());
    }

    @Test
    void delete() {
        reviewStorage.delete(review.getReviewId());
        assertNull(reviewStorage.get(review.getReviewId()).orElse(null));
    }

    @Test
    void getById() {
        assertEquals(review, reviewStorage.get(review.getReviewId()).orElse(null));
    }

    @Test
    void findAll() {
        Review newReview = createNewReview();
        assertArrayEquals(List.of(review, newReview).toArray(), reviewStorage.findAll(film.getId(), 2).toArray());
    }

    private Review createNewReview() {
        Review newReview = Review.builder()
                .content("This film is funny.")
                .isPositive(true)
                .userId(user.getId())
                .filmId(film.getId())
                .build();
        reviewStorage.create(newReview);
        return newReview;
    }
}
