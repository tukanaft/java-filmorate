package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MPARowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MPADbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPARowMapper MPARowMapper;
    private final FilmDbStorage filmDbStorage;

    public List<MPA> getMpas() {
        String query = "Select * from MPA";
        return jdbcTemplate.query(query, MPARowMapper);
    }

    public MPA getMpaById(Integer mpaID) {
        String query = "Select * from MPA where id = ?";
        return jdbcTemplate.queryForObject(query, MPARowMapper, mpaID);
    }

}
