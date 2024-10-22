package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("genreRepository")
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres_names WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres_names";
    private static final String FIND_GENRE_FOR_FILM =
            """
                    SELECT g.id, g.name FROM genres_names AS g
                    JOIN film_genres AS fg
                    ON g.id = fg.genre_id
                    JOIN films AS f
                    ON fg.film_id = f.id
                    WHERE f.id = ?
                    """;

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }


    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }


    public List<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }


    public Set<Genre> getByFilmId(Long id) {
        return new HashSet<>(jdbc.query(FIND_GENRE_FOR_FILM, mapper, id));
    }
}
