package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

/*    @GetMapping
    public List<Review> getReviews() {
        log.info("ReviewController: выполнение запроса на получение отзывов");
        return reviewService.getReviews();
    }

 */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable("id") Integer reviewId) {
        log.info("ReviewController: выполнение запроса на получение отзыва по id: {}", reviewId);
        return reviewService.getReviewById(reviewId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody Review newReview) {
        log.info("ReviewController: выполнение запроса на добавление отзыва: {}", newReview);
        return reviewService.addReview(newReview);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review newReview) {
        log.info("ReviewController: выполнение запроса на обновление отзыва: {}", newReview);
        Review updatedReview = reviewService.updateReview(newReview);
        log.info("ReviewController: запрос на обновление отзыва выполнен: {}", updatedReview);
        return updatedReview;
    }

    @PutMapping("{id}/like/{userId}")
    public Review addLikeToReview(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        log.info("ReviewController: выполнение запроса на установку лайка для отзыва: {} пользователем {}", reviewId, userId);
        return reviewService.addLikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Boolean deleteLikeToReview(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        log.info("ReviewController: выполнение запроса на удаления лайка для отзыва: {} пользователем {}", reviewId, userId);
        return reviewService.deleteLikeToReview(reviewId, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review addDislikeToReview(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        log.info("ReviewController: выполнение запроса на установку дизлайка для отзыва: {} пользователем {}", reviewId, userId);
        return reviewService.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Boolean deleteDislikeToReview(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        log.info("ReviewController: выполнение запроса на удаление дизлайка для отзыва: {} пользователем {}", reviewId, userId);
        return reviewService.deleteDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteReview(@PathVariable("id") Integer reviewId) {
        log.info("ReviewController: выполнение запроса на удаление отзыва: {}", reviewId);
        return reviewService.deleteReview(reviewId);
    }

    @GetMapping
    public List<Review> reviewsOfSelectedFilm(@RequestParam Integer filmId, @RequestParam(defaultValue = "10") Integer count) {
        log.info("ReviewController: выполнение запроса на получение всех отзывов фильма");
        return reviewService.reviewsOfSelectedFilm(filmId, count);
    }
}
