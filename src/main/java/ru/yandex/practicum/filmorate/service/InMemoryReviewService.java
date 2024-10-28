package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.exception.BadInputException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final InMemoryUserService inMemoryUserService;
    private final InMemoryFilmService inMemoryFilmService;

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    @Override
    public Optional<Review> updateReview(Review newReview) {
        if (!reviewRepository.isReviewExists(newReview.getReviewId())) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        return reviewRepository.updateReview(newReview);
    }

    @Override
    public Optional<Review> addLikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.addLikeToReview(reviewId, userId);
    }

    @Override
    public Optional<Review> addDislikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.addDislikeToReview(reviewId, userId);
    }

    @Override
    public Boolean deleteLikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.deleteLikeToReview(reviewId, userId);
    }

    @Override
    public Boolean deleteDislikeToReview(Long reviewId, Long userId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        if (inMemoryUserService.get(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return reviewRepository.deleteDislikeToReview(reviewId, userId);
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
        return reviewRepository.addReview(newReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует");
        }
        reviewRepository.deleteReview(reviewId);
    }

    public List<Review> reviewsOfSelectedFilm(Long filmId, Integer count) {
        return reviewRepository.reviewsOfSelectedFilm(filmId, count);
    }
}
