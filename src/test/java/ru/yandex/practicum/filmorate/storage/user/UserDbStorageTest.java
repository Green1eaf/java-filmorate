package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserDbStorageTest {

    private static final long USER_ID = 1;
    private static final long FRIEND_ID = 2;
    private final UserStorage userStorage;
    private User user;

    @BeforeEach
    public void init() {
        user = new User(null, "email@ya.com", "login", "name",
                LocalDate.of(1986, 3, 14), null);
        userStorage.create(user);
        user.setId(USER_ID);
    }

    @Test
    void create() {
        assertEquals(user, userStorage.get(user.getId()));
    }

    @Test
    void update() {
        var updatedUser = User.builder()
                .id(USER_ID)
                .email("test@ya.com")
                .login("testLogin")
                .name("updateName")
                .birthday(LocalDate.of(1987, 3, 1))
                .build();
        assertEquals(updatedUser, userStorage.update(updatedUser));
    }

    @Test
    void delete() {
        assertEquals(user, userStorage.get(user.getId()));
        userStorage.delete(user.getId());
        assertNull(userStorage.get(user.getId()));
    }

    @Test
    void findAll() {
        assertArrayEquals(List.of(user).toArray(), userStorage.findAll().toArray());
    }

    @Test
    void get() {
        assertEquals(user, userStorage.get(user.getId()));
    }

    @Test
    void addFriendAndFindALlFriends() {
        addFriendsAndFindAllFriendsByIdTest();
    }

    @Test
    void removeFriend() {
        addFriendsAndFindAllFriendsByIdTest();
        userStorage.removeFriend(USER_ID, FRIEND_ID);
        assertArrayEquals(Collections.emptyList().toArray(), userStorage.findAllFriends(USER_ID).toArray());
    }

    private void addFriendsAndFindAllFriendsByIdTest() {
        userStorage.create(User.builder().email("ya@ya.ru").login("log").name("nam")
                .birthday(LocalDate.of(2000, 1, 1)).build());
        userStorage.addFriend(USER_ID, FRIEND_ID);
        assertEquals(List.of(userStorage.get(FRIEND_ID)), userStorage.findAllFriends(USER_ID));
    }
}