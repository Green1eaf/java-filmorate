package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(long id);

    List<User> findAll();

    User get(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> findAllFriends(long id);
}
