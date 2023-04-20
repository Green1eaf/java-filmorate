package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
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
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws Exception {
        repository.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(ValidationException::new);
        repository.removeIf(u -> u.getId().equals(user.getId()));
        repository.add(user);
        return user;
    }
}
