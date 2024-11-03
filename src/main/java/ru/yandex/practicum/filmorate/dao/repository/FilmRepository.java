package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
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
    private static final String SEARCH_BY_FILM = "SELECT * FROM films WHERE LOWER(name) LIKE CONCAT('%',?,'%')";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String SEARCH_BY_FILM_DIRECTOR =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM films AS f
                    JOIN film_directors AS fd ON f.id = fd.film_id
                    JOIN directors_names AS d ON fd.director_id = d.id
                    WHERE LOWER(d.name) LIKE CONCAT('%',?,'%')
                    """;

    private static final String FIND_TOP_FILMS =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN film_user_likes_set AS fuls ON f.id = fuls.film_id
                    GROUP BY f.id
                    ORDER BY COUNT(fuls.film_id) DESC
                    LIMIT ?
                    """;

    private static final String FIND_TOP_FILMS_BY_GENRE_YEAR =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN film_user_likes_set AS ufl ON f.id = ufl.film_id
                    LEFT JOIN film_genres AS fg ON f.id = fg.film_id
                    WHERE (f.release_date BETWEEN ? AND ?) AND fg.genre_id = ?
                    GROUP BY f.id
                    ORDER BY COUNT(ufl.film_id) DESC
                    LIMIT ?
                    """;
    private static final String FIND_TOP_FILMS_BY_GENRE =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN film_user_likes_set AS ufl ON f.id = ufl.film_id
                    LEFT JOIN film_genres AS fg ON f.id = fg.film_id
                    WHERE fg.genre_id = ?
                    GROUP BY f.id
                    ORDER BY COUNT(ufl.film_id) DESC
                    LIMIT ?
                    """;
    private static final String FIND_TOP_FILMS_BY_YEAR =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN film_user_likes_set AS ufl ON f.id = ufl.film_id
                    WHERE f.release_date BETWEEN ? AND ?
                    GROUP BY f.id
                    ORDER BY COUNT(ufl.film_id) DESC
                    LIMIT ?
                    """;
    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY =
            """
                    SELECT id, name, description, release_date, duration, rating_id
                    FROM films AS f
                    LEFT JOIN film_directors AS fd ON f.id = fd.film_id
                    WHERE fd.director_id = ?
                    GROUP BY id, name, description, release_date, duration, rating_id
                    ORDER BY f.release_date
                    """;

    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM films AS f
                    LEFT JOIN film_user_likes_set fuls ON f.id = fuls.film_id
                    JOIN film_directors fd ON f.id = fd.film_id
                    WHERE fd.director_id = ?
                    GROUP BY id, name, description, release_date, duration, rating_id
                    ORDER BY COUNT(fuls.user_id) DESC ;
                    """;

    private static final String FIND_COMMON_FILMS = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
            "f.rating_id FROM films f JOIN film_user_likes_set u1 ON f.id = u1.film_id JOIN film_user_likes_set u2 " +
            "ON f.id = u2.film_id WHERE u1.user_id = ? AND u2.user_id = ?";

    private static final String FIND_ALL_FILMS_LIKED_USER = "SELECT * FROM films WHERE id IN (SELECT film_id " +
            "FROM film_user_likes_set WHERE user_id = ?) ORDER BY id ASC;";

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

    public List<Film> getTopFilmsByGenreYear(int count, Long genreId, LocalDate date) {
        if (date.isEqual(LocalDate.ofYearDay(0, 1))) {
            return jdbc.query(FIND_TOP_FILMS_BY_GENRE, mapper, genreId, count);
        } else if (genreId == 0) {
            return jdbc.query(FIND_TOP_FILMS_BY_YEAR, mapper, date, date.plusYears(1), count);
        } else {
            return jdbc.query(FIND_TOP_FILMS_BY_GENRE_YEAR, mapper, date, date.plusYears(1), genreId, count);
        }
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

    public List<Film> searchByFilm(String query) {
        return jdbc.query(SEARCH_BY_FILM, mapper, query);
    }

    public List<Film> searchByDirector(String query) {
        return jdbc.query(SEARCH_BY_FILM_DIRECTOR, mapper, query);
    }

    public List<Film> getDirectorsFilmSortByYear(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY, mapper, id);
    }

    public List<Film> getDirectorsFilmSortByLikes(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY, mapper, id);
    }

    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        return findMany(FIND_COMMON_FILMS, userId, friendId);
    }

    public Collection<Film> getLikedFilmsByUserId(Long userId) {
        return findMany(FIND_ALL_FILMS_LIKED_USER, userId);
    }
}
