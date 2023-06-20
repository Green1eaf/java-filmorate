package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserEvent;

import java.util.List;

@Repository
public class UserEventDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserEventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(UserEvent userEvent) {
        String sql = "INSERT INTO user_events (timestamp, user_id, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userEvent.getTimestamp(), userEvent.getUserId(), userEvent.getEventType(), userEvent.getOperation(), userEvent.getEntityId());
    }

    public List<UserEvent> findByUserIdOrderByTimestampDesc(Long userId) {
        String sql = "SELECT * FROM user_events WHERE user_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(UserEvent.class));
    }
}