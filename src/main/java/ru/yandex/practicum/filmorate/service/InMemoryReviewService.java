package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.exception.BadInputException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final InMemoryUserService inMemoryUserService;
    private final InMemoryFilmService inMemoryFilmService;
    private final EventService eventService;

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    @Override
    public Optional<Review> updateReview(Review newReview) {
        if (!reviewRepository.isReviewExists(newReview.getReviewId())) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        eventService.addEvent(new Event(newReview.getUserId(), EventType.REVIEW, OperationType.UPDATE, newReview.getReviewId(), Instant.now().toEpochMilli()));
        return reviewRepository.updateReviewWithoutUseful(newReview);
    }

    @Override
    public Optional<Review> addLikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.addLikeOrDislikeToReview(reviewId, userId, "лайк");
    }

    @Override
    public Optional<Review> addDislikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.addLikeOrDislikeToReview(reviewId, userId, "дизлайк");
    }

    @Override
    public Optional<Review> deleteLikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.deleteLikeOrDislikeToReview(reviewId, userId, "лайк");
    }

    @Override
    public Optional<Review> deleteDislikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.deleteLikeOrDislikeToReview(reviewId, userId, "дизлайк");
    }

    @Override
    public Review addReview(Review newReview) {
        if (newReview.getContent() == null) {
            throw new BadInputException("Контент отзыва не может быть пустым");
        }
        if (newReview.getUserId() == null) {
            throw new BadInputException("Не указан ID пользователя");
        }
        if (newReview.getFilmId() == null) {
            throw new BadInputException("Не указан ID фильма");
        }
        if (newReview.getIsPositive() == null) {
            throw new BadInputException("Не указан ID фильма");
        }
        if (inMemoryUserService.get(newReview.getUserId()) == null) {
            throw new NotFoundException("Указанного пользователя для отзыва не существует");
        }
        if (inMemoryFilmService.findFilmById(newReview.getFilmId()) == null) {
            throw new NotFoundException("Указанного фильма для отзыва не существует");
        }
        newReview.setUseful(0);
        Review review = reviewRepository.addReview(newReview);
        eventService.addEvent(new Event(newReview.getUserId(), EventType.REVIEW, OperationType.ADD, newReview.getReviewId(), Instant.now().toEpochMilli()));
        return review;
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        Long userId = (reviewRepository.getReviewById(reviewId)).get().getUserId();
        eventService.addEvent(new Event(userId, EventType.REVIEW, OperationType.REMOVE, reviewId, Instant.now().toEpochMilli()));
        reviewRepository.deleteReview(reviewId);
    }

    public List<Review> reviewsOfSelectedFilm(Long filmId, Integer count) {
        return reviewRepository.reviewsOfSelectedFilm(filmId, count);
    }
}
