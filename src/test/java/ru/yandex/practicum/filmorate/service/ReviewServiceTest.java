package ru.yandex.practicum.filmorate.service;

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
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReviewServiceTest {
    private final ReviewService reviewService;
    private final FilmService filmService;
    private final UserService userService;
    private Film film;
    private User user;
    private User friend;
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
        filmService.create(film);

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

        review = Review.builder()
                .content("This film is soo bad.")
                .isPositive(false)
                .userId(user.getId())
                .filmId(film.getId())
                .build();
        reviewService.create(review);
    }

    @Test
    void createReviewWithFailUserId() {
        Review testReview = Review.builder()
                .content("This film is soo bad.")
                .isPositive(false)
                .userId(-1L)
                .filmId(film.getId())
                .build();
        assertThrows(NotExistException.class, () -> reviewService.create(testReview));
    }

    @Test
    void createReviewWithFailFilmId() {
        Review testReview = Review.builder()
                .content("This film is soo bad.")
                .isPositive(false)
                .userId(user.getId())
                .filmId(-1L)
                .build();
        assertThrows(NotExistException.class, () -> reviewService.create(testReview));
    }

    @Test
    void addLikeAndDeleteLike() {
        reviewService.addLike(review.getReviewId(), friend.getId());
        Review testReview = reviewService.get(review.getReviewId());
        assertEquals(1, testReview.getUseful());

        reviewService.deleteLike(review.getReviewId(), friend.getId());
        testReview = reviewService.get(review.getReviewId());
        assertEquals(0, testReview.getUseful());
    }

    @Test
    void addDislikeAndDeleteDislike() {
        reviewService.addDislike(review.getReviewId(), friend.getId());
        Review testReview = reviewService.get(review.getReviewId());
        assertEquals(-1, testReview.getUseful());

        reviewService.deleteDislike(review.getReviewId(), friend.getId());
        testReview = reviewService.get(review.getReviewId());
        assertEquals(0, testReview.getUseful());
    }
}
