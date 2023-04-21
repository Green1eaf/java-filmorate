package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> repository = new ArrayList<>();
    private int counter = 1;

    @GetMapping
    public List<User> findAllUsers() {
        return repository;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            user.setId(counter++);
        }
        repository.add(user);
        log.info("created user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        repository.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.info("Validation failed for user with name: {}", user.getName());
                    return new ValidationException();
                });
        repository.removeIf(u -> u.getId().equals(user.getId()));
        repository.add(user);
        log.info("updated user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }
}
