package ru.yandex.practicum.filmorate.storage.feed;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserEvent;

import java.util.List;

@Repository
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(UserEvent userEvent) {
        String sql = "INSERT INTO user_events (timestamp, user_id, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userEvent.getTimestamp(), userEvent.getUserId(), userEvent.getEventType(),
                userEvent.getOperation(), userEvent.getEntityId());
    }

    @Override
    public List<UserEvent> findByUserIdOrderByTimestampAsc(Long userId) {
        String sql = "SELECT * FROM user_events WHERE user_id = ? ORDER BY event_id ASC";
        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(UserEvent.class));
    }
}
