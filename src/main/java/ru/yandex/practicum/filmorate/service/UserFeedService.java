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

    private final UserEventStorage userEventStorage;
    private final UserDbStorage userDbStorage;

    public UserFeedService(UserEventStorage userEventStorage, UserDbStorage userDbStorage) {
        this.userEventStorage = userEventStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<UserEvent> getUserFeed(Long userId) {
        if (!userDbStorage.existsById(userId)) {
            throw new NotExistException("User with id=" + userId + " does not exist");
        }
        return userEventStorage.findByUserIdOrderByTimestampDesc(userId);
    }
}
