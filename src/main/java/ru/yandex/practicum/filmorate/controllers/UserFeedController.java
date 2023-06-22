package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.service.UserFeedService;

import java.util.List;

@RestController
public class UserFeedController {

    private final UserFeedService userFeedService;

    public UserFeedController(UserFeedService userFeedService) {
        this.userFeedService = userFeedService;
    }

    @GetMapping("/users/{id}/feed")
    public List<UserEvent> getUserFeed(@PathVariable Long id) {
        return userFeedService.getUserFeed(id);
    }
}
