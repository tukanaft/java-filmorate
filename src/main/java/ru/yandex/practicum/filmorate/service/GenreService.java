package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public List<Genre> getGenres() {
        String query = "Select * from genres";
        return jdbcTemplate.query(query, genreRowMapper);
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId > 6 || genreId<0){
            throw new NotFoundException("не существующий жанр", genreId);
        }
        String query = "Select * from genres where id = ?";
        return jdbcTemplate.queryForObject(query, genreRowMapper, genreId);
    }

}
