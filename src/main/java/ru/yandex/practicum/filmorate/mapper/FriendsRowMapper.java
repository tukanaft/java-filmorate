package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendsRowMapper implements RowMapper<Friends> {


    @Override
    public Friends mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friends friends = new Friends();
        friends.setId(resultSet.getInt("id"));
        friends.setUserId(resultSet.getInt("user_id"));
        friends.setFriendId(resultSet.getInt("friend_id"));

        return friends;
    }
}
