package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FeedDbStorageTest {

    private final FeedStorage feedStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private User user;
    private Film film;
    private static final long USER_ID = 1;

    @BeforeEach
    public void init() {
        user = new User(null, "email@ya.com", "login", "name",
                LocalDate.of(1986, 3, 14), null);
        userStorage.create(user);
        user.setId(USER_ID);
        film = new Film(null, "test", "desc", LocalDate.of(2000, 1, 1), 100,
                new Mpa(1L, "G"), 0, Collections.emptyList(), Collections.emptyList());
        filmStorage.create(film);
    }

    @Test
    public void testSave() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(1L);
        userEvent.setUserId(user.getId());
        userEvent.setEventType("LIKE");
        userEvent.setOperation("ADD");
        userEvent.setEntityId(film.getId());

        feedStorage.save(userEvent);

        List<UserEvent> userEvents = feedStorage.findByUserIdOrderByTimestampAsc(user.getId());
        assertThat(userEvents).isNotEmpty();
        assertThat(userEvents.get(0)).isEqualToComparingFieldByField(userEvent);
    }

    @Test
    public void testFindByUserIdOrderByTimestampDesc() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(1L);
        userEvent.setUserId(user.getId());
        userEvent.setEventType("LIKE");
        userEvent.setOperation("REMOVE");
        userEvent.setEntityId(film.getId());
        feedStorage.save(userEvent);

        Long userId = user.getId();
        List<UserEvent> userEvents = feedStorage.findByUserIdOrderByTimestampAsc(userId);
        assertThat(userEvents).isNotEmpty();
        assertThat(userEvents.get(0).getUserId()).isEqualTo(userId);
    }
}
