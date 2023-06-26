package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FeedService feedService;
    private final FilmService filmService;
    private final LikeService likeService;

    public UserService(UserStorage userStorage, @Lazy LikeService likeService,
                       @Lazy FeedService feedService, @Lazy FilmService filmService) {
        this.userStorage = userStorage;
        this.feedService = feedService;
        this.filmService = filmService;
        this.likeService = likeService;
    }

    public User create(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() != null) {
            throw new AlreadyExistException("User with id=" + user.getId() + " already exist");
        }
        userStorage.create(user);
        log.info("created user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }

    public User update(User user) throws ValidationException {
        get(user.getId());
        log.info("updated user with id: {} and name: {}", user.getId(), user.getName());
        return userStorage.update(user);
    }

    public User get(long id) {
        log.info("Get film with id={}", id);
        return Optional.ofNullable(userStorage.get(id))
                .orElseThrow(() -> new NotExistException("User with id=" + id + " not exist"));
    }

    public void removeById(long userId) {
        userStorage.delete(userId);
        log.info("remove user with id={}", userId);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(long id, long friendId) {
        get(id);
        get(friendId);
        userStorage.addFriend(id, friendId);
        log.info("for user with id={} and user with id={} added each other to friends list", id, friendId);
        UserEvent userEvent = UserEvent.builder()
                .userId(id)
                .eventType("FRIEND")
                .operation("ADD")
                .entityId(friendId)
                .build();
        feedService.save(userEvent);
    }

    public void removeFriend(long id, long friendId) {
        userStorage.removeFriend(id, friendId);
        log.info("for user with id={} and user with id={} removed each other from friends list", id, friendId);
        UserEvent userEvent = UserEvent.builder()
                .userId(id)
                .eventType("FRIEND")
                .operation("REMOVE")
                .entityId(friendId)
                .build();
        feedService.save(userEvent);
    }

    public List<User> findAllFriends(long id) {
        get(id);
        log.info("get all friends for user with id={}", id);
        return userStorage.findAllFriends(id);
    }

    public List<User> findCommonFriends(long id, long otherId) {
        log.info("find all commons friend for users id={} and id={}", id, otherId);
        if (id == otherId) {
            throw new AlreadyExistException("User is same");
        }
        var userFriends = Optional.ofNullable(findAllFriends(id)).orElse(Collections.emptyList());
        var otherUserFriends = Optional.ofNullable(findAllFriends(otherId)).orElse(Collections.emptyList());

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());
    }

    public List<Film> getRecommendationFilms(long id) {
        User user = get(id);
        List<Film> films = filmService.findAll();
        List<Film> likedFilms = films.stream()
                .filter(film -> likeService.getAll(film.getId()).contains(user.getId()))
                .collect(Collectors.toList());
        if (films.isEmpty() || likedFilms.isEmpty()) {
            log.info("No recommendation films found");
            return Collections.emptyList();
        }

        Map<User, Integer> userLikesCounts = new HashMap<>();
        likedFilms.forEach(film -> likeService.getAll(film.getId()).stream()
                .filter(idUser -> !idUser.equals(user.getId()))
                .map(this::get)
                .forEach(anotherUser -> userLikesCounts.put(anotherUser, userLikesCounts.getOrDefault(anotherUser, 0) + 1)));

        if (userLikesCounts.isEmpty()) {
            log.info("No other users liked the same films as user {}", user);
            return Collections.emptyList();
        }

        User userMaxLike = Collections.max(userLikesCounts.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        List<Film> recommendedFilms = films.stream()
                .filter(film -> likeService.getAll(film.getId()).contains(userMaxLike.getId()))
                .collect(Collectors.toList());

        recommendedFilms.removeAll(likedFilms);
        return recommendedFilms;
    }

    public boolean isExistsById(long id) {
        Integer count = userStorage.findIdFromUsers(id);
        return count != null && count > 0;
    }
}
