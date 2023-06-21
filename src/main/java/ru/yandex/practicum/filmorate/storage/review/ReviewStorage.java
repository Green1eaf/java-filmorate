package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review create(Review review);

    Optional<Review> update(Review review);

    void delete(Long id);

    Optional<Review> get(Long id);

    List<Review> findAll(Long filmId, Integer count);

    void increaseUseful(Long reviewId);

    void decreaseUseful(Long reviewId);
}
