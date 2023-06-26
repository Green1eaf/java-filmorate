package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.event.UserEventDbStorage;

import java.util.List;

@Service
@Slf4j
public class UserEventService {
    UserEventDbStorage userEventDbStorage;

    public void save(UserEvent userEvent) {
       userEventDbStorage.save(userEvent);
    }


    public List<UserEvent> findByUserIdOrderByTimestampDesc(Long userId) {
        return userEventDbStorage.findByUserIdOrderByTimestampDesc(userId);
    }
}
