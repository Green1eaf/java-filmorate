package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserDbStorage userDbStorage;

    @Test
    public void testFindByUserId() {
       User expected= User.builder()
                .id(10L)
                .email("test@ya.com")
                .login("test10")
                .name("test")
                .birthdate(LocalDate.of(1986, 3, 14))
                .build();
        userDbStorage.add(expected);
        User test = userDbStorage.get(expected.getId());
        Assertions.assertEquals(expected, test);
    }

    @Test
    public void updateUser() {
        User test=  userDbStorage.get(1);
        test.setName("Ivan");
        userDbStorage.update(test);
        User expected = userDbStorage.get(1);
        Assertions.assertEquals("Ivan", expected.getName());
    }

    @Test
    public void deleteUser() {
        Assertions.assertNotNull(userDbStorage.get(1));
        userDbStorage.delete(1);
        Assertions.assertNull(userDbStorage.get(1));
    }
}