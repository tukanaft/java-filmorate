package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Optional;

@Repository("reviewRepository")
public class ReviewRepository extends BaseRepository<Review> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE id = ?";
    private static final String ADD_REVIEW_QUERY = "INSERT INTO reviews (user_id, film_id, content, useful, isPositive) values(?,?,?,?,?)";
    private static final String ADD_DISLIKE_QUERY = "INSERT INTO reviews_likes (review_id, user_id, liketype) values(?,?,?)";
    private static final String ADD_LIKE_QUERY = "INSERT INTO reviews_likes (review_id, user_id, liketype) values(?,?,?)";
    private static final String DELETE_DISLIKE_QUERY = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ? and liketype = ?";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ? and liketype = ?";
    private static final String DELETE_REVIEW_BY_ID_QUERY = "DELETE FROM reviews WHERE id = ?";
    private static final String GET_REVIEW_FOR_FILM_BY_ID_QUERY = "SELECT id, content, user_id, film_id, isPositive, useful FROM reviews WHERE film_id = ?";
    private static final String GET_REVIEW_FOR_ALL_FILMS_QUERY = "SELECT id, content, user_id, film_id, isPositive, useful FROM reviews";
    private static final String CHECK_REVIEW_EXISTS_QUERY = "SELECT COUNT(*) FROM reviews WHERE id =?";
    private static final String CHECK_LIKE_EXISTS_QUERY = "SELECT COUNT(*) FROM reviews_likes WHERE review_id=? and user_id=? and liketype = ?";
    private static final String CHECK_DISLIKE_EXISTS_QUERY = "SELECT COUNT(*) FROM reviews_likes WHERE review_id =? and user_id =? and liketype = ?";
    private static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content = ?, isPositive=? WHERE id = ?";

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

    public Optional<Review> updateReview(Review newReview) {

        if (isReviewExists(newReview.getReviewId())) {
            update(
                    UPDATE_REVIEW_QUERY,
                    newReview.getContent(),
                    newReview.getIsPositive(),
                    newReview.getReviewId()
            );
        } else {
            throw new NotFoundException("Отзыв который вы пытаетесь обновить не существует");
        }
        return getReviewById(newReview.getReviewId());
    }

    public Optional<Review> addLikeToReview(Long reviewId, Long userId) {
        if (!isLikeExists(reviewId, userId)) {
            jdbc.update(ADD_LIKE_QUERY, reviewId, userId, "лайк");
            Optional<Review> updatedReview = getReviewById(reviewId);
            //Увеличиваем рейтинг отзыва, т.к. ему поставили лайк
            if (updatedReview.isPresent()) {
                updatedReview.get().setUseful(updatedReview.get().getUseful() + 1);
                //Записываем обновление рейтинга в отзыв
                updatedReview = updateReview(updatedReview.get());
            }
            return updatedReview;
        }
        return Optional.empty();
    }

    public Optional<Review> addDislikeToReview(Long reviewId, Long userId) {
        if (!isDislikeExists(reviewId, userId)) {
            jdbc.update(ADD_DISLIKE_QUERY, reviewId, userId, "дизлайк");
            //Review updatedReview = getReviewById(keyHolder.getKey().intValue());
            Optional<Review> updatedReview = getReviewById(reviewId);
            //Уменьшаем рейтинг отзыва, т.к. ему поставили дизлайк
            if (updatedReview.isPresent()) {
                updatedReview.get().setUseful(updatedReview.get().getUseful() - 1);
            }
            //Записываем обновление рейтинга в отзыв
            updatedReview = updateReview(updatedReview.get());
            return updatedReview;
        }
        return null;
    }

    public void deleteReview(Long reviewId) {
        if (isReviewExists(reviewId)) {
            jdbc.update(DELETE_REVIEW_BY_ID_QUERY, reviewId);
        }
    }

    public List<Review> reviewsOfSelectedFilm(Long filmId, Integer count) {
        Long id;
        String query = null;
        if (filmId != null) {
            id = filmId;
            query = GET_REVIEW_FOR_FILM_BY_ID_QUERY;
            return findMany(query, id);
        } else {
            query = GET_REVIEW_FOR_ALL_FILMS_QUERY;
            return findMany(query);
        }
    }

    public boolean deleteLikeToReview(Long reviewId, Long userId) {
        if (isLikeExists(reviewId, userId)) {
            if (isReviewExists(reviewId)) {
                jdbc.update(DELETE_LIKE_QUERY, reviewId, userId, "лайк");
                Optional<Review> updatedReview = getReviewById(reviewId);
                if (updatedReview.isPresent()) {
                    updatedReview.get().setUseful(updatedReview.get().getUseful() - 1);
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteDislikeToReview(Long reviewId, Long userId) {
        if (isLikeExists(reviewId, userId)) {
            if (isReviewExists(reviewId)) {
                jdbc.update(DELETE_DISLIKE_QUERY, reviewId, userId, "дизлайк");
                Optional<Review> updatedReview = getReviewById(reviewId);
                if (updatedReview.isPresent()) {
                    updatedReview.get().setUseful(updatedReview.get().getUseful() + 1);
                }
                return true;
            }
        }
        return false;
    }

    public Boolean isReviewExists(Long userId) {
        int count = jdbc.queryForObject(CHECK_REVIEW_EXISTS_QUERY, Integer.class, userId);
        return count > 0;
    }

    public Boolean isLikeExists(Long reviewId, Long userId) {
        String liketype = "лайк";
        Integer count = jdbc.queryForObject(CHECK_LIKE_EXISTS_QUERY, Integer.class, reviewId, userId, liketype);
        return count > 0;
    }

    public Boolean isDislikeExists(Long reviewId, Long userId) {
        String liketype = "дизлайк";
        Integer count = jdbc.queryForObject(CHECK_DISLIKE_EXISTS_QUERY, Integer.class, reviewId, userId, liketype);
        return count > 0;
    }
}
