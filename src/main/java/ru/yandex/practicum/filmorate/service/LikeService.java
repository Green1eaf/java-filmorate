package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.List;

@Service
@Slf4j
public class LikeService {
    private final LikeDbStorage likeDbStorage;
    private final UserService userService;
    private final FeedService feedService;
    private final FilmService filmService;

    public LikeService(LikeDbStorage likeDbStorage, UserService userService, FeedService feedService, FilmService filmService) {
        this.likeDbStorage = likeDbStorage;
        this.userService = userService;
        this.feedService = feedService;
        this.filmService = filmService;
    }

    public List<Long> getAll(long id) {
        return likeDbStorage.getAll(id);
    }

    public void add(Long userId, Long filmId, Long mark) {
        likeDbStorage.add(userId, filmId, mark);
    }

    public void remove(long userId, long filmId) {
        likeDbStorage.remove(userId, filmId);
    }

    public void like(Long filmId, Long userId, Long mark) {
        if (filmId == null || userId == null || mark == null) {
            throw new BadRequestException("Недостаточно данных, необходимо указать фильм, свой id и оценку");
        }

        userService.checkExisting(userId);
        filmService.checkExisting(filmId);
        add(userId, filmId, mark);
        log.info("like = {} for film with id={} from user with id={}", mark, filmId, userId);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("ADD")
                .entityId(filmId)
                .build();
        feedService.save(userEvent);
    }

    public void removeLike(long filmId, long userId) {
        userService.checkExisting(userId);
        filmService.checkExisting(filmId);
        remove(userId, filmId);
        log.info("remove like from film with id={}, from user with id={}", filmId, userId);
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventType("LIKE")
                .operation("REMOVE")
                .entityId(filmService.getById(filmId).getId())
                .build();
        feedService.save(userEvent);
    }

    public List<Long> getRecommendations(long userId) {
        return likeDbStorage.getRecommendations(userId);
    }
}
