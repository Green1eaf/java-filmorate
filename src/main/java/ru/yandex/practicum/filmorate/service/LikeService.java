package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.List;

@Service
@Slf4j
public class LikeService {
    LikeDbStorage likeDbStorage;
    UserService userService;
    FeedService feedService;
    FilmService filmService;

    public LikeService(LikeDbStorage likeDbStorage, UserService userService, FeedService feedService, FilmService filmService) {
        this.likeDbStorage = likeDbStorage;
        this.userService = userService;
        this.feedService = feedService;
        this.filmService = filmService;
    }

    public List<Long> getAll(long id) {
        return likeDbStorage.getAll(id);
    }

    public void add(long userId, long filmId) {
        likeDbStorage.add(userId, filmId);
    }

    public void remove(long userId, long filmId) {
        likeDbStorage.remove(userId, filmId);
    }

    public void like(long filmId, long userId) {
        userService.get(userId);
        filmService.getById(filmId);
        add(userId, filmId);
        log.info("like for film with id={} from user with id={}", filmId, userId);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("ADD")
                .entityId(filmId)
                .build();
        feedService.save(userEvent);
    }

    public void removeLike(long id, long userId) {
        userService.get(userId);
        filmService.getById(id);
        remove(userId, id);
        log.info("remove like from film with id={}, from user with id={}", id, userId);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("REMOVE")
                .entityId(filmService.getById(id).getId())
                .build();
        feedService.save(userEvent);
    }

}
