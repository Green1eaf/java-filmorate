package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        return Optional.ofNullable(userStorage.get(id))
                .orElseThrow(() -> new NotExistException("User with id=" + id + " not exist"));
    }

    public void addFriend(long id, long friendId) {
        get(id);
        get(friendId);
        userStorage.addFriend(id, friendId);
        log.info("for user with id={} and user with id={} added each other to friends list", id, friendId);
    }

    public void removeFriend(long id, long friendId) {
        userStorage.removeFriend(id, friendId);
        log.info("for user with id={} and user with id={} removed each other from friends list", id, friendId);
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
}
