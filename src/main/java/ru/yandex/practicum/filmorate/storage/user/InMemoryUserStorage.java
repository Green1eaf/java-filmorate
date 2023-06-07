package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User create(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        return create(user);
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User get(long id) {
        return storage.get(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        storage.get(userId).getFriends().add(friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        storage.get(userId).getFriends().remove(friendId);
    }

    @Override
    public List<User> findAllFriends(long id) {
        return storage.get(id).getFriends().stream()
                .map(storage::get)
                .collect(Collectors.toList());
    }
}
