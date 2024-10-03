package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.LikeRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final LikeRowMapper likeRowMapper;

    @Override
    public User addUser(User newUser) {
        String query = "INSERT INTO users (name,login,birthday) values(?,?,?)";
        newUser.setId(jdbcTemplate.update(query, newUser.getName(), newUser.getLogin(), newUser.getBirthday()));
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (isUserExists(newUser.getId())) {
            String query = "UPDATE users SET name = ?, login=?, birthday = ? WHERE id = ?";
            jdbcTemplate.update(query, newUser.getName(), newUser.getLogin(), newUser.getBirthday(), newUser.getId());
        } else {
            throw new NotFoundException("фильм который вы пытаетесь обновить не существует", newUser.getId());
        }
        return newUser;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> usersUpload = new HashMap<>();
        String query = "SELECT * from users";
        List<User> users = jdbcTemplate.query(query, userRowMapper);
        for (User user : users) {
            insertFriends(user);
            usersUpload.put(user.getId(), user);
        }
        return usersUpload;
    }

    @Override
    public Boolean addFriend(Integer userId, Integer friendsId) {
        if (isAlreadyFriend(friendsId, userId)) {
            return changeStatus(friendsId, userId);
        }
        String query = "INSERT INTO friends (user_id,friend_id,status) values(?,?,?)";
        jdbcTemplate.update(query, userId, friendsId, Status.UNAPPROVED.toString());
        return true;
    }

    @Override
    public Boolean deleteFriend(Integer userId, Integer friendsId) {
        if (isAlreadyFriend(userId, friendsId)) {
            if (isAlreadyFriend(friendsId, userId)) {
                String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
                jdbcTemplate.update(query, friendsId, userId);
                return true;
            }
            String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(query, userId, friendsId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isUserExists(Integer userId) {
        String query = "SELECT COUNT(*) FROM users WHERE id =?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{userId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public ArrayList<Integer> getFriends(Integer userId) {
        ArrayList<Integer> friendList = new ArrayList<>();
        String queryFriendId = "SELECT friend_id from friends g WHERE user_id = ?";
        List<Integer> friendsId = jdbcTemplate.queryForList(queryFriendId, new Object[]{userId}, Integer.class);
        String queryFriendId2 = "SELECT friend_id from friends g WHERE friend_id = ?";
        List<Integer> friendsId2 = jdbcTemplate.queryForList(queryFriendId2, new Object[]{userId}, Integer.class);
        if (friendsId != null) {
            friendList.addAll(friendsId);
        }
        if (friendsId2 != null) {
            friendList.addAll(friendsId2);
        }
        return friendList;
    }

    @Override
    public void clear() {

    }

    private Boolean isAlreadyFriend(Integer userId, Integer friends_id) {
        String query = "SELECT COUNT(*) FROM friends WHERE user_id =? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{userId, friends_id}, Integer.class);
        return count != null && count > 0;
    }

    private Boolean changeStatus(Integer userId, Integer friendsId) {
        String query = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(query, Status.APPROVED.toString(), userId, friendsId);
        return true;
    }

    private User insertFriends(User user) {
        Integer count = 0;
        HashMap<Integer, Status> friends = new HashMap<>();
        String queryFriendId = "SELECT friend_id from friends g WHERE user_id = ?";
        String queryStatus = "SELECT status from friends WHERE user_id =?";
        List<Integer> friendsId = jdbcTemplate.queryForList(queryFriendId, new Object[]{user.getId()}, Integer.class);
        List<String> status = jdbcTemplate.queryForList(queryStatus, new Object[]{user.getId()}, String.class);
        for (int i = 0; i < friendsId.size(); i++) {
            friends.put(friendsId.get(i), Status.valueOf(status.get(i)));
            count++;
        }
        String queryFriendId2 = "SELECT friend_id from friends g WHERE friend_id = ?";
        String queryStatus2 = "SELECT status from friends WHERE friend_id =?";
        List<Integer> friendsId2 = jdbcTemplate.queryForList(queryFriendId2, new Object[]{user.getId()}, Integer.class);
        List<String> status2 = jdbcTemplate.queryForList(queryStatus2, new Object[]{user.getId()}, String.class);
        for (int i = 0; i < friendsId.size(); i++) {
            friends.put(friendsId2.get(count), Status.valueOf(status2.get(count)));
            count++;
        }
        user.setFriendsId(friends);
        return user;
    }
}
