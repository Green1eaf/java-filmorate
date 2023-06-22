package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {

    List<Integer> getAll(Integer filmId);

    void add(long userId, long filmId);

    void remove(long userId, long filmId);
}
