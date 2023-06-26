package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class LikeService {
    private final LikeDbStorage likeDbStorage;
    private final FilmService filmService;
    private final UserService userService;
    private final UserEventService userEventService;

    public LikeService(LikeDbStorage likeDbStorage, FilmService filmService, UserService userService, UserEventService userEventService) {
        this.likeDbStorage = likeDbStorage;
        this.filmService = filmService;
        this.userService = userService;
        this.userEventService = userEventService;
    }

    public void addLike(long filmId, long userId, long mark) {
        userService.get(userId);
        filmService.getById(filmId);
        likeDbStorage.add(filmId, userId, mark);
        log.info("like for film with id={} from user with id={}", filmId, userId, mark);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("ADD")
                .entityId(filmId)
                .build();
        userEventService.save(userEvent);

    }

    public void removeLike(long filmId, long userId) {
        userService.get(userId);
        filmService.getById(filmId);
        likeDbStorage.remove(userId, filmId);
        log.info("remove like from film with id={}, from user with id={}", filmId, userId);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("REMOVE")
                .entityId(filmService.getById(filmId).getId())
                .build();
        userEventService.save(userEvent);
    }

    public Set<Long> getAll(long userId) {
        return likeDbStorage.getAll(userId);
    }

    public List<Long> getRecommendations(long userId) {
        return likeDbStorage.getRecommendations(userId);
    }
}
