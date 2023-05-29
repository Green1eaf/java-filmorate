package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        jdbcTemplate.update("INSERT INTO users (id, email, login, name, birthdate) VALUES (?, ?, ?, ?, ?)",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthdate());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthdate=? WHERE id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthdate(), user.getId());
        return user;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User get(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class)).stream()
                .findFirst()
                .orElse(null);
    }

    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update("INSERT INTO friendly_relations (user_id, friend_id) VALUES (?,?)", userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update("DELETE FROM friendly_relations WHERE user_id=? AND friend_id=?", userId, friendId);
    }
}
