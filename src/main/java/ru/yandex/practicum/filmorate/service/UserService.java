package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

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
            //TODO обработать исключение
            System.out.println("Пользователь уже существует");
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
                    log.info("validation failed for user with name: {}", user.getName());
                    return new ValidationException();
                });
    }

    public User get(int id) {
        return userStorage.get(id);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public void addFriend(long id, long friendId) {
        userStorage.get(id).addFriend(friendId);
        log.info("for user with id={} added user with id={} in friends list", id, friendId);
        userStorage.get(friendId).addFriend(id);
        log.info("for user with id={} added user with id={} in friends list", friendId, id);
    }

    public void removeFriend(long id, long friendId) {
        userStorage.get(id).removeFriend(friendId);
        log.info("for user with id={} removed user with id={} from friends list", id, friendId);
        userStorage.get(friendId).removeFriend(id);
        log.info("for user with id={} removed user with id={} from friends list", friendId, id);
    }

    public List<User> findAllFriends(long id) {
        List<User> friendsList = new ArrayList<>();
        userStorage.get(id).getFriends().forEach(userId -> friendsList.add(userStorage.get(userId)));
        log.info("get all friends for user with id={}", id);
        return friendsList;
    }

    public List<User> findCommonFriends(long id, long otherId) {
        var common = findAllFriends(id);
        common.retainAll(findAllFriends(otherId));
        log.info("find all commons friend for users id={} and id={}", id, otherId);
        return common;
    }
}
