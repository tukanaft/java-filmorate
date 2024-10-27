package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.ReviewDbStorage;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDbStorage reviewDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    public List<Review> getReviews(){
        log.info("ReviewService: выполнение запроса на получение отзывов");
        return reviewDbStorage.getReviews();
    }
    public Review getReviewById(Integer reviewId){
        log.info("ReviewService: выполнение запроса на получение отзыва по id {}", reviewId);
        return reviewDbStorage.getReviewById(reviewId);
    }

    public Review updateReview(Review newReview){
        log.info("ReviewService: выполнение запроса на обновление отзыва: {}", newReview);
        if (!reviewDbStorage.isReviewExists(newReview.getReviewId())) {
            throw new NotFoundException("Такого отзыва не существует", newReview.getReviewId());
        }
        return reviewDbStorage.updateReview(newReview);
    }

    public Review addLikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewService: выполнение запроса на добавление лайка для отзыва: {}", reviewId);
        if (!reviewDbStorage.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует", reviewId);
        }
        if (!userDbStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует", userId);
        }
        return reviewDbStorage.addLikeToReview(reviewId, userId);
    }

    public Review addDislikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewService: выполнение запроса на добавление дизлайка для отзыва: {}", reviewId);
        if (!reviewDbStorage.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует", reviewId);
        }
        if (!userDbStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует", userId);
        }
        return reviewDbStorage.addDislikeToReview(reviewId, userId);
    }

    public Boolean deleteLikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewService: выполнение запроса на удаление лайка для отзыва: {}", reviewId);
        if (!reviewDbStorage.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует", reviewId);
        }
        if (!userDbStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует", userId);
        }
        return reviewDbStorage.deleteLikeToReview(reviewId, userId);
    }

    public Boolean deleteDislikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewService: выполнение запроса на удаление дизлайка для отзыва: {}", reviewId);
        if (!reviewDbStorage.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует", reviewId);
        }
        if (!userDbStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует", userId);
        }
        return reviewDbStorage.deleteDislikeToReview(reviewId, userId);
    }

    public Review addReview(Review newReview) {
        log.info("ReviewService: выполнение запроса на добавление отзыва: {}", newReview);
        if(newReview.getContent()==null){
            throw new ValidationException("Контент отзыва не может быть пустым");
        }
        if(!userDbStorage.isUserExists(newReview.getUserId())){
            throw new NotFoundException("Указанного пользователя для отзыва не существует", newReview.getUserId());
        }
        if(!filmDbStorage.isFilmExists(newReview.getFilmId())){
            throw new NotFoundException("Указанного фильма для отзыва не существует", newReview.getFilmId());
        }
        newReview.setReviewRate(0);
        log.info("ReviewService: выполнение запроса на добавление отзыва завершёно");
        return reviewDbStorage.addReview(newReview);
    }

    public boolean deleteReview(Integer reviewId){
        log.info("ReviewService: выполнение запроса на удаление отзыва");
        if (!reviewDbStorage.isReviewExists(reviewId)) {
            throw new NotFoundException("Такого отзыва не существует", reviewId);
        }
        log.info("ReviewService: выполнение запроса на удаление отзыва завершено");
        return reviewDbStorage.deleteReview(reviewId);
    }

    public List<Review> reviewsOfSelectedFilm(Integer filmId, Integer count){
        log.info("ReviewService: выполнение запроса на получение отзывов по выбранному фильму");
        return reviewDbStorage.reviewsOfSelectedFilm(filmId, count);
    }
}
