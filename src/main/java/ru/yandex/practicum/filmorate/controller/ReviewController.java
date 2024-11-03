package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.InMemoryReviewService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final InMemoryReviewService reviewService;

    @GetMapping("/{id}")
    public Optional<Review> getReviewById(@PathVariable("id") Long reviewId) {
        log.info("ReviewController: Выполняется запрос на полчение отзыва по ID");
        return reviewService.getReviewById(reviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody Review newReview) {
        log.info("ReviewController: Выполняется запрос на создание отзыва");
        return reviewService.addReview(newReview);
    }

    @PutMapping
    public Optional<Review> updateReview(@RequestBody Review newReview) {
        log.info("ReviewController: Выполняется запрос на обновление отзыва");
        return reviewService.updateReview(newReview);
    }

    @PutMapping("{id}/like/{userId}")
    public Optional<Review> addLikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("ReviewController: Выполняется запрос на добавление лайка отзыву");
        return reviewService.addLikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Optional<Review> deleteLikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("ReviewController: Выполняется запрос на удаление лайка у отзыва");
        return reviewService.deleteLikeToReview(reviewId, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Optional<Review> addDislikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("ReviewController: Выполняется запрос на добавление дизлайка отзыву");
        return reviewService.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Optional<Review> deleteDislikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("ReviewController: Выполняется запрос на удаление дизлайка у отзыва");
        return reviewService.deleteDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Long reviewId) {
        log.info("ReviewController: Выполняется запрос на удаление отзыва по ID");
        reviewService.deleteReview(reviewId);
    }

    @GetMapping
    public List<Review> reviewsOfSelectedFilm(@RequestParam(defaultValue = "0") Long filmId, @RequestParam(defaultValue = "10") Integer count) {
        log.info("ReviewController: Выполняется запрос на полчение отзывов по фильму");
        return reviewService.reviewsOfSelectedFilm(filmId, count);
    }
}
