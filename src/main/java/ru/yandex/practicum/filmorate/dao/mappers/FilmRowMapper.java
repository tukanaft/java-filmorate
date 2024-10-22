package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component("filmRowMapper")
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    final GenreRepository genreRepository;
    final RatingRepository ratingRepository;
    final DirectorRepository directorRepository;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        Long ratingId = resultSet.getLong("rating_id");

        Rating mpa = ratingRepository.get(ratingId)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + ratingId + " не существует"));
        film.setMpa(mpa);

        Set<Genre> genres = genreRepository.getByFilmId(film.getId());
        film.setGenres(genres);

        Set<Director> directors = directorRepository.getByFilmId(film.getId());
        film.setDirectors(directors);
        return film;
    }
}
