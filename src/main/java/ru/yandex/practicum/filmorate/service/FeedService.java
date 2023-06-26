package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@Slf4j
public class FeedService {

    private final FeedStorage feedStorage;
    private final UserService userService;

    public FeedService(FeedStorage feedStorage, UserService userService) {
        this.feedStorage = feedStorage;
        this.userService = userService;
    }

    public List<UserEvent> getUserFeed(Long userId) {
        if (!userService.isExistsById(userId)) {
            throw new NotExistException("User with id=" + userId + " does not exist");
        }
        return feedStorage.findByUserIdOrderByTimestampAsc(userId);
    }

    public void save(UserEvent userEvent) {
        feedStorage.save(userEvent);
    }
}
