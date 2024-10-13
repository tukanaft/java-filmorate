package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.RaitingRowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RatingDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RaitingRowMapper raitingRowMapper;
    private final FilmDbStorage filmDbStorage;

    public List<MPA> getRaitings() {
        String query = "Select * from ratings";
        return jdbcTemplate.query(query, raitingRowMapper);
    }

    public MPA getRaitingById(Integer raitingId) {
        String query = "Select * from ratings where id = ?";
        return jdbcTemplate.queryForObject(query, raitingRowMapper, raitingId);
    }

}
