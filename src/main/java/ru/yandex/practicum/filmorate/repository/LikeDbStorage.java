package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.LikeRowMapper;

@RequiredArgsConstructor
@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikeRowMapper likeMapper;

    @Override
    public Boolean isAlreadyLiked(Integer filmId, Integer userId) {
        String query = "SELECT COUNT(*) FROM likes WHERE film_id =? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{filmId, userId}, Integer.class);
        return count != null && count > 0;
    }

}
