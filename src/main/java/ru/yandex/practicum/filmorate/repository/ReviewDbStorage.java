package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReviewDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper ReviewRowMapper;

    public List<Review> getReviews() {
        String query = "SELECT r.id, r.content, r.user_id, r.film_id, r.isPositive, r.useful FROM reviews r";

        return jdbcTemplate.query(query, ReviewRowMapper);
    }

    public Review updateReview(Review newReview){
        log.info("ReviewDbStorage: Обновлене отзыва");
        if (isReviewExists(newReview.getReviewId())) {
            log.info("Обновляю отзыв: {}", newReview);
            String query = "UPDATE reviews SET content = ?, isPositive=?, reviewRate=? WHERE id = ?";
            jdbcTemplate.update(query, newReview.getContent(), newReview.isPositive(), newReview.getReviewId(), newReview.getReviewRate());
        } else {
            throw new NotFoundException("Отзыв который вы пытаетесь обновить не существует", newReview.getReviewId());
        }
        return newReview;
    }

    public Review addLikeToReview(Integer reviewId, Integer userId){
        if(!isLikeExists(reviewId,userId)) {
            String query = "INSERT INTO reviews_likes (review_id, user_id, liketype) values(?,?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, reviewId);
                statement.setInt(2, userId);
                statement.setString(3, "лайк");
                return statement;
            }, keyHolder);
            //Review updatedReview = getReviewById(keyHolder.getKey().intValue());
            Review updatedReview = getReviewById(reviewId);
            log.info("ReviewDbStorage: Добавляю лайк к отзыву: {}", updatedReview);
            //Увеличиваем рейтинг отзыва, т.к. ему поставили лайк
            updatedReview.setReviewRate(updatedReview.getReviewRate() + 1);
            log.info("ReviewDbStorage: Добавил лайк (+1) к отзыву: {}", updatedReview);
            //Записываем обновление рейтинга в отзыв
            updatedReview = updateReview(updatedReview);
            return updatedReview;
        }
        log.info("ReviewDbStorage: Этот пользователь уже ставил лайк этому отзыву");
        return null;
    }

    public boolean deleteLikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewDbStorage: Удаление лайка для отзыва: {}",reviewId);
        if(isLikeExists(reviewId,userId)) {
            if (isReviewExists(reviewId)) {
                String query = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ? and liketype = ?";
                jdbcTemplate.update(query, reviewId, userId, "лайк");
                Review updatedReview = getReviewById(reviewId);
                updatedReview.setReviewRate(updatedReview.getReviewRate() - 1);
                log.info("ReviewDbStorage: Лайк для отзыва: {} добавлен",reviewId);
                return true;
            }
        }
        return false;
    }

    public boolean deleteDislikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewDbStorage: Удаление дизлайка для отзыва: {}",reviewId);
        if(isDislikeExists(reviewId,userId)) {
            if (isReviewExists(reviewId)) {
                String query = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ? and liketype = ?";
                jdbcTemplate.update(query, reviewId, userId, "дизлайк");
                Review updatedReview = getReviewById(reviewId);
                updatedReview.setReviewRate(updatedReview.getReviewRate() + 1);
                log.info("ReviewDbStorage: Дизлайк для отзыва: {} удалён",reviewId);
                return true;
            }
        }
        return false;
    }
    public Review addDislikeToReview(Integer reviewId, Integer userId){
        log.info("ReviewDbStorage: Добавление дизлайка для отзыва: {}",reviewId);
        if(!isDislikeExists(reviewId,userId)) {
            String query = "INSERT INTO reviews_likes (review_id, user_id, liketype) values(?,?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, reviewId);
                statement.setInt(2, userId);
                statement.setString(3, "дизлайк");
                log.info("ReviewDbStorage: Дизлайк для отзыва: {} добавлен",reviewId);
                return statement;
            }, keyHolder);
            //Review updatedReview = getReviewById(keyHolder.getKey().intValue());
            Review updatedReview = getReviewById(reviewId);
            //Уменьшаем рейтинг отзыва, т.к. ему поставили дизлайк
            if (updatedReview.getReviewRate() > 0) {
                updatedReview.setReviewRate(updatedReview.getReviewRate() - 1);
            }
            //Записываем обновление рейтинга в отзыв
            updatedReview = updateReview(updatedReview);
            return updatedReview;
        }
        return null;
    }

    public Review getReviewById(Integer reviewId){
        log.info("ReviewDbStorage: Получение отзыва по id: {}",reviewId);
        if (isReviewExists(reviewId)) {
            String query = "SELECT * FROM reviews WHERE id ="+reviewId;
            log.info("ReviewDbStorage: query - {}",query);
            return jdbcTemplate.queryForObject(query, ReviewRowMapper);
        }else{
            throw new NotFoundException("Такого отзыва в базе нет", reviewId);
        }
    }

    public Review addReview(Review newReview) {
        log.info("ReviewDbStorage: Добавление в базу объекта отзыв: {}",newReview);
        String query = "INSERT INTO reviews (user_id, film_id, content, useful, isPositive, reviewRate) values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, newReview.getUserId());
            statement.setInt(2, newReview.getFilmId());
            statement.setString(3, newReview.getContent());
            statement.setInt(4, newReview.getUseful());
            statement.setBoolean(5, newReview.isPositive());
            statement.setInt(6, 0);
            log.info("ReviewDbStorage: Добавление отзыва успешно");
            return statement;
        }, keyHolder);
        return getReviewById(keyHolder.getKey().intValue());
    }

    public Boolean deleteReview(Integer reviewId) {
        log.info("ReviewDbStorage: Удаление отзыва");
        if (isReviewExists(reviewId)) {
            String query = "DELETE FROM reviews WHERE id = ?";
            jdbcTemplate.update(query, reviewId);
            log.info("ReviewDbStorage: Удаление отзыва успешно");
            return true;
        }
        return false;
    }

    public List<Review> reviewsOfSelectedFilm(Integer filmId, Integer count){
        Integer id = 0;
        String query = null;
        if(filmId!=null){
            id = filmId;
            query = "SELECT r.id, r.content, r.user_id, r.film_id, r.isPositive, r.useful, r.reviewRate FROM reviews r WHERE film_id ="+id;
        }else{
            query = "SELECT r.id, r.content, r.user_id, r.film_id, r.isPositive, r.useful, r.reviewRate FROM reviews r ";
        }
        return jdbcTemplate.query(query, ReviewRowMapper);
    }

    public Boolean isReviewExists(Integer userId) {
        log.info("ReviewDbStorage: Проверяю, есть ли уже такой отзыв");
        String query = "SELECT COUNT(*) FROM reviews WHERE id =?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{userId}, Integer.class);
        return count != null && count > 0;
    }

    public Boolean isLikeExists(Integer reviewId, Integer userId) {
        log.info("ReviewDbStorage: Проверяю, есть ли такой лайк у такого пользователя");
        String liketype = "лайк";
        String query = "SELECT COUNT(*) FROM reviews_likes WHERE review_id="+reviewId+" and user_id="+userId+" and liketype='"+liketype+"'";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class);
        return count != null && count > 0;
    }

    public Boolean isDislikeExists(Integer reviewId, Integer userId) {
        log.info("ReviewDbStorage: Проверяю, есть ли такой дизлайк у такого пользователя");
        String liketype = "дизлайк";
        String query = "SELECT COUNT(*) FROM reviews_likes WHERE review_id ="+reviewId+" and user_id ="+userId+" and liketype = '"+liketype+"'";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class);
        return count != null && count > 0;
    }


}
