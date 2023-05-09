package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private long counter = 1;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        if (user.getId() == null) {
            user.setId(counter++);
        } else {
            throw new AlreadyExistException("User with id=" + user.getId() + " already exist");
        }
        userStorage.add(user);
        log.info("created user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }

    public User update(User user) throws ValidationException {
        return userStorage.findAll().stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .map(u -> {
                    userStorage.delete(u.getId());
                    userStorage.add(user);
                    log.info("updated user with id: {} and name: {}", user.getId(), user.getName());
                    return user;
                })
                .orElseThrow(() -> {
                    throw new ValidationException("validation failed for user with name: " + user.getName());
                });
    }

    public User get(long id) {
        return Optional.of(userStorage.get(id))
                .orElseThrow(() -> {
                    throw new NotExistException("User with id=" + id + " not exist");
                });
    }

    public void addFriend(long id, long friendId) {
        var user = get(id);
        get(friendId).addFriend(id);
        user.addFriend(friendId);
        log.info("for user with id={} and user with id={} added each other to friends list", id, friendId);
    }

    public void removeFriend(long id, long friendId) {
        var user = get(id);
        get(friendId).removeFriend(id);
        user.removeFriend(friendId);
        log.info("for user with id={} and user with id={} removed each other from friends list", id, friendId);
    }

    public List<User> findAllFriends(long id) {
        log.info("get all friends for user with id={}", id);
        return findAll().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(long id, long otherId) {
        var common = findAllFriends(id);
        common.retainAll(findAllFriends(otherId));
        log.info("find all commons friend for users id={} and id={}", id, otherId);
        return common;
    }
}
