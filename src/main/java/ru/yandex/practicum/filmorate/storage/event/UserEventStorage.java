package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.UserEvent;
import java.util.List;

public interface UserEventStorage {
    void save(UserEvent userEvent);
    List<UserEvent> findByUserIdOrderByTimestampDesc(Long userId);
}
