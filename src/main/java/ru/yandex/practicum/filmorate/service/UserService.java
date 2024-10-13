package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User newUser);

    ArrayList<User> getUsers();

    Boolean addFriend(Integer userId, Integer friendsId);

    Boolean deleteFriend(Integer userId, Integer friendsId);

    List<User> commonFriends(Integer userId, Integer friendsId);

    void clear();

    List<User> getFriends(Integer userId);
}
