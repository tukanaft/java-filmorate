package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("userRepository")
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    private static final String ADD_FRIEND_QUERY =
            "INSERT INTO user_friends(user_id, friend_id, is_accepted) VALUES (?, ?, false)";

    private static final String DELETE_FRIEND_QUERY =
            "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";

    private static final String IS_FRIEND_REQUEST =
            "SELECT * FROM user_friends WHERE friend_id = ? AND user_id = ? AND is_accepted = false";

    private static final String IS_FRIEND =
            "SELECT * FROM user_friends WHERE friend_id = ? AND user_id = ? AND is_accepted = true";

    private static final String ACCEPT_REQUEST =
            "UPDATE user_friends SET is_accepted = true WHERE friend_id = ? AND user_id = ?";

    private static final String REMOVE_REQUEST =
            "UPDATE user_friends SET is_accepted = false WHERE friend_id = ? AND user_id = ?";

    private static final String GET_FRIENDS_QUERY =
            """
                    SELECT * FROM users WHERE id IN (
                    SELECT friend_id FROM user_friends
                    WHERE user_id = ?
                    UNION ALL
                    SELECT user_id FROM user_friends
                    WHERE friend_id = ? AND is_accepted = true)
                    """;

    private static final String GET_COMMON_FRIENDS =
            """
                    SELECT * FROM users WHERE id IN (
                    SELECT * FROM (
                    SELECT friend_id  FROM user_friends
                    WHERE user_id = ? OR user_id = ?
                    UNION ALL
                    SELECT user_id FROM user_friends
                    WHERE (friend_id = ? OR friend_id = ?) AND is_accepted = true) as cf
                    GROUP BY friend_id
                    HAVING COUNT(*) > 1)
                    """;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    public boolean addFriend(Object... params) {
        return jdbc.update(ADD_FRIEND_QUERY, params) > 0;
    }

    public boolean deleteFriend(Object... params) {
        return jdbc.update(DELETE_FRIEND_QUERY, params) > 0;
    }

    public boolean isFriendRequest(Object... params) {
        return jdbc.queryForRowSet(IS_FRIEND_REQUEST, params).next();
    }

    public boolean acceptRequest(Object... params) {
        return jdbc.update(ACCEPT_REQUEST, params) > 0;
    }

    public boolean isFriend(Object... params) {
        return jdbc.queryForRowSet(IS_FRIEND, params).next();
    }

    public boolean removeRequest(Object... params) {
        return jdbc.update(REMOVE_REQUEST, params) > 0;
    }

    public Set<User> getFriends(Long id) {
        List<User> users = jdbc.query(GET_FRIENDS_QUERY, mapper, id, id);
        return new HashSet<>(users);
    }

    public Set<User> getCommonFriends(Object... params) {
        List<User> users = jdbc.query(GET_COMMON_FRIENDS, mapper, params[0], params[1], params[0], params[1]);
        return new HashSet<>(users);
    }
}
