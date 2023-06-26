package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

public interface LikeStorage {

    Set<Long> getAll(long filmId);

    void add(long userId, long filmId, long mark);

    void remove(long userId, long filmId);
}
