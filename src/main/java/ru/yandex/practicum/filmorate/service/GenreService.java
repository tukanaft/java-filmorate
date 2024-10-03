package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    JdbcTemplate jdbcTemplate;
    GenreRowMapper genreRowMapper;

    public List<Genre> getGenres() {
        String query = "Select * from genres";
        return jdbcTemplate.query(query, genreRowMapper);
    }

    public Genre getGenreById(Integer genreId) {
        String query = "Select * from genres where id = ?";
        return jdbcTemplate.queryForObject(query, genreRowMapper, genreId);
    }

}
