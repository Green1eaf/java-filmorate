package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.UserEvent;
import java.util.List;

public interface FeedStorage {

    void save(UserEvent userEvent);

    List<UserEvent> findByUserIdOrderByTimestampAsc(Long userId);
}
