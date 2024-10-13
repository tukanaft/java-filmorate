package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public List<Genre> getGenres() {
        String query = "Select * from genres";
        return jdbcTemplate.query(query, genreRowMapper);
    }

    public Genre getGenreById(Integer genreId) {
        String query = "Select * from genres where id = ?";
        return jdbcTemplate.queryForObject(query, genreRowMapper, genreId);
    }
}
