package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDtoRowMapper implements RowMapper<FilmDto> {

    @Override
    public FilmDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmDto film = new FilmDto();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setRaiting(resultSet.getString("raiting"));


        return film;
    }
}
