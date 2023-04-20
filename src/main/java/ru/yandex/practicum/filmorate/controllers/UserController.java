package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> repository = new ArrayList<>();

    @GetMapping
    public List<User> findAllUsers() {
        return repository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        repository.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        repository.removeIf(u -> u.getId() == user.getId());
        repository.add(user);
        return user;
    }
}
