package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.event.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Service
@Slf4j
public class UserFeedService {

    private final UserEventService userEventService;
    private final UserService userService;

    public UserFeedService(UserEventService userEventService,  UserService userService) {
        this.userEventService = userEventService;
        this.userService = userService;
    }

    public List<UserEvent> getUserFeed(Long userId) {
        if (!userService.existsById(userId)) {
            throw new NotExistException("User with id=" + userId + " does not exist");
        }
        return userEventService.findByUserIdOrderByTimestampDesc(userId);
    }
}
