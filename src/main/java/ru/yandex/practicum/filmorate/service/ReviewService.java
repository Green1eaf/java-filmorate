package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;

    public ReviewService(ReviewStorage reviewStorage, FilmService filmService, UserService userService) {
        this.reviewStorage = reviewStorage;
        this.filmService = filmService;
        this.userService = userService;
    }

    public Review create(Review review) {
        filmService.get(review.getFilmId());
        userService.get(review.getUserId());
        reviewStorage.create(review);
        log.info("added review with id: {} from user id: {} for film id: {}", review.getReviewId(), review.getUserId(), review.getFilmId());
        return review;
    }

    public Optional<Review> update(Review review) {
        get(review.getReviewId());
        log.info("updated review with id: {} from user id: {} for film id: {}", review.getReviewId(), review.getUserId(), review.getFilmId());
        return reviewStorage.update(review);
    }

    public void delete(Long id) {
        Review review = get(id);
        log.info("deleted review with id: {} from user id: {} for film id: {}", id, review.getUserId(), review.getFilmId());
        reviewStorage.delete(id);
    }

    public Review get(Long id) {
        log.info("received review with id: {}", id);
        return reviewStorage.get(id).orElseThrow(() -> new NotExistException("review with id=" + id + " not exists"));
    }

    public List<Review> findAll(Long filmId, Integer count) {
        log.info("received all reviews");
        return reviewStorage.findAll(filmId, count);
    }

    public void addLike(Long reviewId, Long userId) {
        reviewStorage.increaseUseful(reviewId);
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        reviewStorage.increaseUseful(reviewId);
    }
}
