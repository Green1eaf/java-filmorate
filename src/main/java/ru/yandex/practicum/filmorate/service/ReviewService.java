package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.event.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;
    private final UserEventStorage userEventStorage;

    public ReviewService(ReviewStorage reviewStorage, FilmService filmService, UserService userService, UserEventStorage userEventDbStorage) {
        this.reviewStorage = reviewStorage;
        this.filmService = filmService;
        this.userService = userService;
        this.userEventStorage = userEventDbStorage;
    }

    public Review create(Review review) {
        filmService.getById(review.getFilmId());
        userService.get(review.getUserId());
        reviewStorage.create(review);
        log.info("added review with id: {} from user id: {} for film id: {}", review.getReviewId(), review.getUserId(), review.getFilmId());
        UserEvent userEvent = UserEvent.builder()
                .userId(review.getUserId())
                .eventType("REVIEW")
                .operation("ADD")
                .entityId(review.getReviewId())
                .build();
        userEventStorage.save(userEvent);
        return review;
    }

    public Optional<Review> update(Review review) {
        Review existingReview = get(review.getReviewId());
        long previousUserId = existingReview.getUserId();
        long previousEntityId = existingReview.getFilmId();
        log.info("updated review with id: {} from user id: {} for film id: {}", review.getReviewId(), review.getUserId(), review.getFilmId());
        UserEvent userEvent = UserEvent.builder()
                .userId(previousUserId)
                .eventType("REVIEW")
                .operation("UPDATE")
                .entityId(previousEntityId)
                .build();
        userEventStorage.save(userEvent);
        return reviewStorage.update(review);
    }

    public void delete(Long id) {
        Review review = get(id);
        log.info("deleted review with id: {} from user id: {} for film id: {}", id, review.getUserId(), review.getFilmId());
        UserEvent userEvent = UserEvent.builder()
                .userId(review.getUserId())
                .eventType("REVIEW")
                .operation("REMOVE")
                .entityId(review.getReviewId())
                .build();
        userEventStorage.save(userEvent);
        reviewStorage.delete(id);
    }

    public Review get(Long id) {
        log.info("received review with id: {}", id);
        return reviewStorage.get(id).orElseThrow(() -> new NotExistException("review with id=" + id + " not exists"));
    }

    public List<Review> findAllByFilmIdOrAll(Long filmId, Integer count) {
        log.info("received all reviews");
        return reviewStorage.findAll(filmId, count);
    }

    public void addLike(Long reviewId, Long userId) {
        log.info("Added like to review id: {} from user id: {}", reviewId, userId);
        reviewStorage.increaseUseful(reviewId);
    }

    public void addDislike(Long reviewId, Long userId) {
        log.info("Added dislike to review id: {} from user id: {}", reviewId, userId);
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        log.info("Deleted like to review id: {} from user id: {}", reviewId, userId);
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        log.info("Deleted dislike to review id: {} from user id: {}", reviewId, userId);
        reviewStorage.increaseUseful(reviewId);
    }
}