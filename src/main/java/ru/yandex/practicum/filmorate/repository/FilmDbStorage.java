package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmDtoRowMapper;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDtoRowMapper filmDtoRowMapper;
    private final FilmRowMapper filmRowMapper;
    private final MpaRowMapper MPARowMapper;
    private final GenreRowMapper genreRowMapper;


    @Override
    public Film addFilm(Film newFilm) {
        String query = "INSERT INTO films (name,description,release_date,duration,mpa_id) values(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (!ifMPAExists(newFilm.getMpa().getId())) {
            throw new ValidationException("такого рейтинга не существует");
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, new String[]{"id"});
            statement.setString(1, newFilm.getName());
            statement.setString(2, newFilm.getDescription());
            statement.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
            statement.setInt(4, newFilm.getDuration());
            statement.setInt(5, newFilm.getMpa().getId());
            return statement;
        }, keyHolder);
        newFilm.setId(keyHolder.getKey().intValue());

        if (newFilm.getGenres() != null) {

            ifGanreExists(newFilm.getGenres());
            List<Genre> genres = newFilm.getGenres();

            for (int i = 0; i < newFilm.getGenres().size(); i++) {
                String queryGenre = "INSERT INTO film_genres (film_id, genres_Id) values(?,?)";
                jdbcTemplate.update(queryGenre, newFilm.getId(), genres.get(i).getId());
            }
        }
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (isFilmExists(newFilm.getId())) {
            String query = "UPDATE films SET name = ?, description=?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
            jdbcTemplate.update(query, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                    newFilm.getDuration(), newFilm.getMpa().getId(), newFilm.getId());
        } else {
            throw new NotFoundException("фильм который вы пытаетесь обновить не существует", newFilm.getId());
        }
        return newFilm;
    }

    @Override
    public HashMap<Integer, FilmDto> getFilms() {
        HashMap<Integer, FilmDto> filmsUpload = new HashMap<>();
        String query = "SELECT f.id, f.name,f.description,f.release_date,f.duration,f.mpa_id FROM films f LEFT JOIN " + "ratings r on f.rating_id = r.id ";
        List<FilmDto> films = jdbcTemplate.query(query, filmDtoRowMapper);
        for (FilmDto film : films) {
            insertGenresAndLikes(film);
            filmsUpload.put(film.getId(), film);
        }
        return filmsUpload;
    }

    public Film getFilm(Integer filmId) {
        String query = "Select * from films WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(query, filmRowMapper, filmId);
        String queryGenre = "SELECT * from genres g JOIN film_genres fg on fg.genres_id = g.id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(queryGenre, new Object[]{filmId}, genreRowMapper);
        if (genres != null) {
            for (int i = 0; i < genres.size() - 1; i++) {
                for (int j = i + 1; j < genres.size(); j++) {
                    if (genres.get(i).getId().equals(genres.get(j).getId())) {
                        genres.remove(genres.get(j));
                    }
                }
            }
            film.setGenres(genres);
        }
        String queryMpa = "SELECT * from MPA where id =?";
        MPA mpa = jdbcTemplate.queryForObject(queryMpa, MPARowMapper, film.getMpa().getId());
        film.setMpa(mpa);
        return film;
    }

    @Override
    public Boolean like(Integer filmId, Integer userId) {
        String query = "INSERT INTO likes (film_id,user_id) values (?,?)";
        jdbcTemplate.update(query, filmId, userId);
        return true;
    }

    @Override
    public Boolean unlike(Integer filmId, Integer userId) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        return true;
    }


    @Override
    public Boolean isFilmExists(Integer filmId) {
        String query = "SELECT COUNT(*) FROM films WHERE id =?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{filmId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void clear() {

    }

    private void insertGenresAndLikes(FilmDto film) {
        String queryGenre = "SELECT g.name from genres g JOIN film_genres fg on fg.genres_id = g.id WHERE fg.film_id = ?";
        String queryLike = "SELECT user_id from likes WHERE film_id =?";
        List<String> genres = jdbcTemplate.queryForList(queryGenre, new Object[]{film.getId()}, String.class);
        List<Integer> likes = jdbcTemplate.queryForList(queryLike, new Object[]{film.getId()}, Integer.class);
        film.setLikes(likes);
        film.setGenres(genres);
    }

    public Boolean ifMPAExists(Integer mpaId) {
        String query = "SELECT COUNT(*) FROM MPA WHERE id =?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{mpaId}, Integer.class);
        return count != null && count > 0;
    }

    public void ifGanreExists(List<Genre> genres) {
        if (genres == null) {
            throw new ValidationException("жанра не существует");
        }
        for (Genre genre : genres) {
            if (genre.getId() > 6 || genre.getId() < 0) {
                throw new ValidationException("жанра не существует");
            }
        }
    }
}
