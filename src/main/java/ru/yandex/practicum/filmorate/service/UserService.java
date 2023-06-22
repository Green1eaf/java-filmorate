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
import ru.yandex.practicum.filmorate.storage.event.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final UserEventStorage userEventStorage;
    private final FilmService filmService;
    private final LikeDbStorage likeDbStorage;


    public UserService(UserStorage userStorage, LikeDbStorage likeDbStorage, UserEventStorage userEventStorage, @Lazy FilmService filmService) {
        this.userStorage = userStorage;
        this.userEventStorage = userEventStorage;
        this.filmService = filmService;
        this.likeDbStorage = likeDbStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
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
        userEventStorage.save(userEvent);
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
        userEventStorage.save(userEvent);
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

    public void removeById(long userId) {
        userStorage.delete(userId);
        log.info("remove user with id={}", userId);
    }

    public List<Film> getRecommendationFilms(long id) {
        User user = get(id);
        List<Film> films = filmService.findAll();
        List<Film> likedFilms = films.stream()
                .filter(film -> likeDbStorage.getAll(film.getId()).contains(user.getId()))
                .collect(Collectors.toList());
        if (films.isEmpty() || likedFilms.isEmpty()) {
            log.info("No recommendation films found");
            return Collections.emptyList();
        }

        Map<User, Integer> countLikesUser = new HashMap<>();
        likedFilms.forEach(film -> likeDbStorage.getAll(film.getId()).stream()
                .filter(idUser -> !idUser.equals(user.getId()))
                .map(this::get)
                .forEach(user1 -> countLikesUser.put(user1, countLikesUser.getOrDefault(user1, 0) + 1)));

        if (countLikesUser.isEmpty()) {
            log.info("No other users liked the same films as user {}", user);
            return Collections.emptyList();
        }

        User userMaxLike = Collections.max(countLikesUser.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        List<Film> recommendedFilms = films.stream()
                .filter(film -> likeDbStorage.getAll(film.getId()).contains(userMaxLike.getId()))
                .collect(Collectors.toList());

        recommendedFilms.removeAll(likedFilms);
        return recommendedFilms;
    }
}
