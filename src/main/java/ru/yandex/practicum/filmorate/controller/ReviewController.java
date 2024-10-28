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
        return reviewService.getReviewById(reviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody Review newReview) {
        return reviewService.addReview(newReview);
    }

    @PutMapping
    public Optional<Review> updateReview(@RequestBody Review newReview) {
        return reviewService.updateReview(newReview);
    }

    @PutMapping("{id}/like/{userId}")
    public Optional<Review> addLikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        return reviewService.addLikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Boolean deleteLikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        return reviewService.deleteLikeToReview(reviewId, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Optional<Review> addDislikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        return reviewService.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Boolean deleteDislikeToReview(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        return reviewService.deleteDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping
    public List<Review> reviewsOfSelectedFilm(@RequestParam Long filmId, @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.reviewsOfSelectedFilm(filmId, count);
    }
}
