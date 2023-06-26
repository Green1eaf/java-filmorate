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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FeedServiceTest {

    private final FeedStorage feedStorage;
    private final UserService userService;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeService likeService;

    private FeedService feedService;
    private User user;
    private static final long USER_ID = 1;

    @BeforeEach
    public void init() {
        feedService = new FeedService(feedStorage, userService);
        user = new User(null, "email@ya.com", "login", "name",
                LocalDate.of(1986, 3, 14), null);
        userStorage.create(user);
        user.setId(USER_ID);
        Film film = new Film(null, "test", "desc", LocalDate.of(2000, 1, 1), 100,
                new Mpa(1L, "G"), 0, Collections.emptyList(), Collections.emptyList());
        filmStorage.create(film);
        likeService.like(film.getId(), user.getId());
        likeService.removeLike(film.getId(), user.getId());
    }

    @Test
    public void testGetUserFeed() {
        Long userId = user.getId();
        List<UserEvent> userEvents = feedService.getUserFeed(userId);

        assertThat(userEvents).isNotEmpty();

        boolean hasLikeEvent = false;
        boolean hasUnlikeEvent = false;
        for (UserEvent event : userEvents) {
            if ("LIKE".equals(event.getEventType()) && "ADD".equals(event.getOperation())) {
                hasLikeEvent = true;
            }
            if ("LIKE".equals(event.getEventType()) && "REMOVE".equals(event.getOperation())) {
                hasUnlikeEvent = true;
            }
        }

        assertThat(hasLikeEvent).isTrue();
        assertThat(hasUnlikeEvent).isTrue();
    }

    @Test
    public void testGetUserFeedForNonExistingUser() {
        Long nonExistingUserId = 9999L;
        assertThrows(NotExistException.class, () -> feedService.getUserFeed(nonExistingUserId));
    }
}