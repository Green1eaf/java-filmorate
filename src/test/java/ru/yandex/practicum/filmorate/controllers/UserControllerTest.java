package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    private User user;

    @BeforeEach
    public void init() {
        user = new User(1, "mail@mail.com", "mata", "Mata Hari",
                LocalDate.of(1986, 3, 14));
        userController.createUser(user);
    }

    @Test
    void findAllUsers() {
        userController.createUser(user);
        Assertions.assertEquals(5, userController.findAllUsers().size());
    }

    @Test
    void createUser() {
        Assertions.assertEquals(2, userController.findAllUsers().size());
        Assertions.assertEquals(3, userController.findAllUsers().size());
    }

    @Test
    void updateUser() throws ValidationException {
        User testUser = userController.createUser(user);
        testUser.setName("UpdateName");
        userController.updateUser(testUser);
        Assertions.assertArrayEquals(List.of(testUser).toArray(), userController.findAllUsers().toArray());
    }
}