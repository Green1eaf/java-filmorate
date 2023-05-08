package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private int counter = 1;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        if (user.getId() == null) {
            user.setId(counter++);
        } else {
            //TODO обработать исключение
            System.out.println("Пользователь уже существует");
        }
        userStorage.add(user);
        log.info("created user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }

    public User update(User user) throws ValidationException {
        return userStorage.findAll().stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .map(u -> {
                    userStorage.delete(u.getId());
                    userStorage.add(user);
                    log.info("updated user with id: {} and name: {}", user.getId(), user.getName());
                    return user;
                })
                .orElseThrow(() -> {
                    log.info("validation failed for user with name: {}", user.getName());
                    return new ValidationException();
                });
    }

    public User get(int id) {
        return userStorage.get(id);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }
}
