package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public Optional<Review> getReviewById(Long reviewId);

    public Optional<Review> updateReview(Review newReview);

    public Optional<Review> addLikeToReview(Long reviewId, Long userId);

    public Optional<Review> addDislikeToReview(Long reviewId, Long userId);

    public Optional<Review> deleteLikeToReview(Long reviewId, Long userId);

    public Optional<Review> deleteDislikeToReview(Long reviewId, Long userId);

    public Review addReview(Review newReview);

    public void deleteReview(Long reviewId);

    public List<Review> reviewsOfSelectedFilm(Long filmId, Integer count);

}
