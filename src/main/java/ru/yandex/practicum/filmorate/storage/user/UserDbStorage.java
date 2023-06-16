package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (email, login, name, birthdate) " +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthdate=? WHERE id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    @Transactional
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public User get(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{id},
                        new UserMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update("INSERT INTO friendly_relations (user_id, friend_id) VALUES (?,?)", userId, friendId);
    }

    @Transactional
    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update("DELETE FROM friendly_relations WHERE user_id=? AND friend_id=?", userId, friendId);
    }

    public List<User> findAllFriends(long id) {
        return jdbcTemplate.query("SELECT u.* FROM users u " +
                        "JOIN friendly_relations f ON f.friend_id=u.id " +
                        "WHERE f.user_id=?",
                new UserMapper(), id);
    }
}
