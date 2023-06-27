package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {

    List<Long> getAll(long filmId);

    void add(long filmId, long userId, long mark);

    void remove(long userId, long filmId);
}
