package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {

    List<Long> getAll(long filmId);

    void add(Long filmId, Long userId, Long mark);

    void remove(long userId, long filmId);

    List<Long> getRecommendations(long userId);
}
