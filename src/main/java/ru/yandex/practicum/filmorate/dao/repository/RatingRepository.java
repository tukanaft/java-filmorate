package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;
import java.util.Optional;

@Repository("ratingRepository")
public class RatingRepository extends BaseRepository<Rating> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Rating> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Rating> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
