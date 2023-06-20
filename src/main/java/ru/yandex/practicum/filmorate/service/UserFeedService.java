package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.user.UserEventDbStorage;

import java.util.List;

@Service
public class UserFeedService {

    private final UserEventDbStorage userEventDbStorage;

    public UserFeedService(UserEventDbStorage userEventDbStorage) {
        this.userEventDbStorage = userEventDbStorage;
    }

    public List<UserEvent> getUserFeed(Long userId) {
        return userEventDbStorage.findByUserIdOrderByTimestampDesc(userId);
    }
}
