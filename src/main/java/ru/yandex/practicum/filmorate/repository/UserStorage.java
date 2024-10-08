package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    User addUser(User newUser);

    User updateUser(User newUser);

    HashMap<Integer, User> getUsers();

    Boolean addFriend(Integer user, Integer friendsId);

    Boolean deleteFriend(Integer user, Integer friendsId);

    Boolean isUserExists(Integer filmId);

    ArrayList<Integer> getFriends(Integer userId);

    void clear();
}
