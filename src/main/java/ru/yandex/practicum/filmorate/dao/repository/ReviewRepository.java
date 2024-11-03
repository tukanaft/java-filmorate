package ru.yandex.practicum.filmorate.dao.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Optional;

@Repository("reviewRepository")
@Slf4j
public class ReviewRepository extends BaseRepository<Review> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE id = ?";
    private static final String ADD_REVIEW_QUERY = "INSERT INTO reviews (user_id, film_id, content, useful, isPositive) values(?,?,?,?,?)";
    private static final String ADD_LIKE_OR_DISLIKE_QUERY = "INSERT INTO reviews_likes (review_id, user_id, liketype) values(?,?,?)";
    private static final String DELETE_LIKE_OR_DISLIKE_QUERY = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ? and liketype = ?";
    private static final String DELETE_REVIEW_BY_ID_QUERY = "DELETE FROM reviews WHERE id = ?";
    private static final String DELETE_REVIEWS_LIKES_BY_REVIEW_ID_QUERY = "DELETE FROM reviews_likes WHERE review_id = ?";
    private static final String GET_REVIEW_FOR_FILM_BY_ID_QUERY = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";
    private static final String GET_REVIEW_FOR_ALL_FILMS_QUERY = "SELECT * FROM reviews ORDER BY useful DESC LIMIT ?";
    private static final String CHECK_REVIEW_EXISTS_QUERY = "SELECT COUNT(*) FROM reviews WHERE id =?";
    private static final String CHECK_LIKE_OR_DISLIKE_EXISTS_QUERY = "SELECT COUNT(*) FROM reviews_likes WHERE review_id=? and user_id=? and liketype = ?";
    private static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content = ?, isPositive=? WHERE id = ?";
    private static final String UPDATE_REVIEW_WITH_USEFUL_QUERY = "UPDATE reviews SET content = ?, isPositive=?, useful=? WHERE id = ?";


    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Review> getReviewById(Long id) {
        if (isReviewExists(id)) {
            return findOne(FIND_BY_ID_QUERY, id);
        } else {
            throw new NotFoundException("Такого отзыва в базе нет");
        }
    }

    public Review addReview(Review newReview) {
        long id = insert(
                ADD_REVIEW_QUERY,
                newReview.getUserId(),
                newReview.getFilmId(),
                newReview.getContent(),
                newReview.getUseful(),
                newReview.getIsPositive()
        );
        newReview.setReviewId(id);
        return newReview;

    }

    public Optional<Review> updateReviewWithoutUseful(Review newReview) {
        update(
                UPDATE_REVIEW_QUERY,
                newReview.getContent(),
                newReview.getIsPositive(),
                newReview.getReviewId()
        );
        return getReviewById(newReview.getReviewId());
    }

    public Optional<Review> updateReview(Review newReview) {
        if (isReviewExists(newReview.getReviewId())) {
            if (newReview.getUseful() != null) {
                update(
                        UPDATE_REVIEW_WITH_USEFUL_QUERY,
                        newReview.getContent(),
                        newReview.getIsPositive(),
                        newReview.getUseful(),
                        newReview.getReviewId()
                );
            } else {
                update(
                        UPDATE_REVIEW_QUERY,
                        newReview.getContent(),
                        newReview.getIsPositive(),
                        newReview.getReviewId()
                );
            }
        } else {
            throw new NotFoundException("Отзыв который вы пытаетесь обновить не существует");
        }
        return getReviewById(newReview.getReviewId());
    }

    public Optional<Review> addLikeOrDislikeToReview(Long reviewId, Long userId, String likeType) {
        if (!isLikeOrDislikeExists(reviewId, userId, likeType)) {
            jdbc.update(ADD_LIKE_OR_DISLIKE_QUERY, reviewId, userId, likeType);
            Optional<Review> reviewToBeUpdated = getReviewById(reviewId);
            if (reviewToBeUpdated.isPresent()) {
                if (likeType.equals("лайк")) {
                    //Если лайк, то поднимаем рейтинг отзыва
                    reviewToBeUpdated.get().setUseful(reviewToBeUpdated.get().getUseful() + 1);
                } else {
                    //Если дизлайк, то понижаем рейтинг отзыва
                    reviewToBeUpdated.get().setUseful(reviewToBeUpdated.get().getUseful() - 1);
                    //Теперь проверяем, если этот же пользователь ставил раньше лайк, то его тоже убираем, т.е. ещё -1 (как в YouTube)
                    if (isLikeOrDislikeExists(reviewId, userId, "лайк")) {
                        reviewToBeUpdated.get().setUseful(reviewToBeUpdated.get().getUseful() - 1);
                    }
                }
                //Обновив рейтинг отзыва, сохраняем его в базе
                reviewToBeUpdated = updateReview(reviewToBeUpdated.get());
            }
            return reviewToBeUpdated;
        }
        return Optional.empty();
    }

    public void deleteReview(Long reviewId) {
        jdbc.update(DELETE_REVIEWS_LIKES_BY_REVIEW_ID_QUERY, reviewId);
        jdbc.update(DELETE_REVIEW_BY_ID_QUERY, reviewId);
    }

    public List<Review> reviewsOfSelectedFilm(Long filmId, Integer count) {
        if (filmId != 0) {
            return jdbc.query(GET_REVIEW_FOR_FILM_BY_ID_QUERY, mapper, filmId, count);
        } else {
            return jdbc.query(GET_REVIEW_FOR_ALL_FILMS_QUERY, mapper, count);
        }
    }

    public Optional<Review> deleteLikeOrDislikeToReview(Long reviewId, Long userId, String likeType) {
        if (isLikeOrDislikeExists(reviewId, userId, likeType)) {
            if (isReviewExists(reviewId)) {
                jdbc.update(DELETE_LIKE_OR_DISLIKE_QUERY, reviewId, userId, likeType);
                Optional<Review> updatedReview = getReviewById(reviewId);
                if (updatedReview.isPresent()) {
                    if (likeType.equals("лайк")) {
                        updatedReview.get().setUseful(updatedReview.get().getUseful() - 1);
                    } else {
                        updatedReview.get().setUseful(updatedReview.get().getUseful() + 1);
                    }
                    updatedReview = updateReview(updatedReview.get());
                }
                return updatedReview;
            }
        }
        return Optional.empty();
    }

    public Boolean isReviewExists(Long userId) {
        Integer count = jdbc.queryForObject(CHECK_REVIEW_EXISTS_QUERY, Integer.class, userId);
        return count != null && count > 0;
    }

    public Boolean isLikeOrDislikeExists(Long reviewId, Long userId, String likeType) {
        Integer count = jdbc.queryForObject(CHECK_LIKE_OR_DISLIKE_EXISTS_QUERY, Integer.class, reviewId, userId, likeType);
        return count != null && count > 0;
    }
}
