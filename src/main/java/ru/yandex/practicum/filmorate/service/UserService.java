package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User newUser);

    ArrayList<User> getUsers();

    Boolean addFriend(Integer userId, Integer friendsId);

    Boolean deleteFriend(Integer userId, Integer friendsId);

    ArrayList<User> commonFriends(Integer userId, Integer friendsId);

    void clear();

    ArrayList<Integer> getFriends(Integer userId);
}
