package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {


    @Override
    public FilmGenre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setId(resultSet.getInt("id"));
        filmGenre.setFilmId(resultSet.getInt("film_id"));
        filmGenre.setGernreId(resultSet.getInt("genre_id"));

        return filmGenre;
    }
}