package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review create(Review review) {
        String sqlQuery = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setLong(3, review.getUserId());
            ps.setLong(4, review.getFilmId());
            return ps;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Optional<Review> update(Review review) {
        if (review == null) {
            throw new NotExistException("Empty argument passed!");
        }
        String sqlQuery = "UPDATE reviews " +
                "SET content = ?, is_positive = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return get(review.getReviewId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reviews WHERE id=?", id);
    }

    @Override
    public Optional<Review> get(Long id) {
        return jdbcTemplate.query("SELECT * FROM reviews WHERE id=?", new ReviewMapper(), id).stream().findFirst();
    }

    @Override
    public List<Review> findAll(Long filmId, Integer count) {
        return (filmId == null) ?
                jdbcTemplate.query("SELECT * FROM reviews ORDER BY useful DESC", new ReviewMapper()) :
                jdbcTemplate.query("SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC",
                                new ReviewMapper(), filmId).stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseUseful(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET useful = useful + 1 WHERE id = ?", reviewId);
    }

    @Override
    public void decreaseUseful(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET useful = useful - 1 WHERE id = ?", reviewId);
    }
}
