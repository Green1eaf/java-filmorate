package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {
    void add(long userId, long filmId);

    void delete(long userId, long filmId);

    List<Long> getAll(long filmId);
}
