package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

@Repository("filmRepository")
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String ADD_LIKE = "INSERT INTO film_user_likes_set(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM film_user_likes_set WHERE film_id = ? AND user_id = ? ";
    private static final String FIND_LIKE = "SELECT * FROM film_user_likes_set WHERE film_id = ? AND user_id = ? ";
    private static final String ADD_GENRE_FOR_FILM = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?) ";
    private static final String DELETE_ALL_GENRE_FOR_FILM = "DELETE FROM film_genres WHERE film_id = ? ";
    private static final String ADD_DIRECTOR_FOR_FILM = "INSERT INTO film_directors(film_id, director_id) VALUES (?, ?) ";
    private static final String DELETE_ALL_DIRECTOR_FOR_FILM = "DELETE FROM film_directors WHERE film_id = ? ";
    private static final String SEARCH_FILM = "SELECT * FROM films WHERE LOWER(name) LIKE CONCAT('%',?,'%')";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String SEARCH_FILM_DIRECTOR =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM films AS f
                    JOIN film_directors AS fd ON f.id = fd.film_id
                    JOIN directors AS d ON fd.director_id = d.id
                    WHERE LOWER(d.name) LIKE CONCAT('%',?,'%')
                    """;

    private static final String FIND_TOP_FILMS =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN film_user_likes_set AS ufl ON f.id = ufl.film_id
                    GROUP BY f.id
                    ORDER BY COUNT(ufl.film_id) DESC
                    LIMIT ?
                    """;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film newFilm) {
        insert(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    public boolean putLike(Long id, Long userId) {
        return jdbc.update(ADD_LIKE, id, userId) > 0;
    }

    public boolean deleteLike(Long id, Long userId) {
        return jdbc.update(DELETE_LIKE, id, userId) > 0;
    }

    public boolean findLike(Long id, Long userId) {
        return jdbc.queryForRowSet(FIND_LIKE, id, userId).next();
    }

    public List<Film> getTopFilms(int count) {
            return jdbc.query(FIND_TOP_FILMS, mapper, count);
    }

    public void addGenreForFilm(Long id, Long genreId) {
        jdbc.update(ADD_GENRE_FOR_FILM, id, genreId);
    }

    public void deleteAllGenresForFilm(Long id) {
        jdbc.update(DELETE_ALL_GENRE_FOR_FILM, id);
    }

    public void addDirectorForFilm(Long id, Long directorId) {
        jdbc.update(ADD_DIRECTOR_FOR_FILM, id, directorId);
    }

    public void deleteAllDirectorsForFilm(Long id) {
        jdbc.update(DELETE_ALL_DIRECTOR_FOR_FILM, id);
    }

    public List<Film> getSearchFilm(String query) {
        return jdbc.query(SEARCH_FILM, mapper, query);
    }

    public List<Film> getSearchDirector(String query) {
        return jdbc.query(SEARCH_FILM_DIRECTOR, mapper, query);
    }
}
