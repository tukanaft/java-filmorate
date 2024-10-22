package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("directorRepository")
public class DirectorRepository extends BaseRepository<Director> {
    private static final String INSERT_QUERY = "INSERT INTO directors_names(name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM directors_names WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM directors_names";
    private static final String DELETE_QUERY = "DELETE FROM directors_names WHERE id = ?";
    private static final String UPDATE_QUERY = " UPDATE directors_names SET name = ? WHERE id = ?";
    private static final String GET_DIRECTOR_FOR_FILM =
            """
                    SELECT d.id, d.name FROM directors_names AS d
                    JOIN film_directors AS fd ON d.id = fd.director_id
                    JOIN films AS f ON fd.film_id = f.id
                    WHERE f.id = ?
                    """;

    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Director> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    public Set<Director> getByFilmId(Long id) {
        return new HashSet<>(jdbc.query(GET_DIRECTOR_FOR_FILM, mapper, id));
    }

    public Director save(Director director) {
        Long id = insert(
                INSERT_QUERY,
                director.getName()
        );
        director.setId(id);
        return director;
    }

    public Director update(Director newDirector) {
        update(
                UPDATE_QUERY,
                newDirector.getName(),
                newDirector.getId()
        );
        return newDirector;
    }
}
