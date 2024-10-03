package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmDtoRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDtoRowMapper filmDtoRowMapper;


    @Override
    public Film addFilm(Film newFilm) {
        String query = "INSERT INTO films (name,description,release_date,duration,raiting_id) values(?,?,?,?,?)";
        newFilm.setId(jdbcTemplate.update(query, newFilm.getName(),
                newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration(), newFilm.getRaiting().getId()));
        List<Integer> genresID = newFilm.getGenre();
        for (int i = 0; i < newFilm.getGenre().size(); i++) {
            String queryGenre = "INSERT INTO film_genres (film_id, genres_Id) values(?,?)";
            jdbcTemplate.update(queryGenre, newFilm.getId(), genresID.get(i));
        }
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (isFilmExists(newFilm.getId())) {
            String query = "UPDATE films SET name = ?, description=?, release_date = ?, duration = ?, raiting_id = ? WHERE id = ?";
            jdbcTemplate.update(query, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                    newFilm.getDuration(), newFilm.getRaiting().getId(), newFilm.getId());
        } else {
            throw new NotFoundException("фильм который вы пытаетесь обновить не существует", newFilm.getId());
        }
        return newFilm;
    }

    @Override
    public HashMap<Integer, FilmDto> getFilms() {
        HashMap<Integer, FilmDto> filmsUpload = new HashMap<>();
        String query = "SELECT f.id, f.name,f.description,f.release_date,f.duration,f.raiting FROM films f LEFT JOIN " +
                "raitings r on f.raiting_id = r.id ";
        List<FilmDto> films = jdbcTemplate.query(query, filmDtoRowMapper);
        for (FilmDto film : films) {
            insertGenresAndLikes(film);
            filmsUpload.put(film.getId(), film);
        }
        return filmsUpload;
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
        film.setGenre(genres);
    }
}