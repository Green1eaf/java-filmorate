package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User add(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        return add(user);
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
}
