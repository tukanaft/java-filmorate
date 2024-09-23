package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface UserStorage {
    User addUser(User newUser);

    User updateUser(User newUser);

    ArrayList<User> getUsers();

    User addFriend(Integer user, Integer friendsId);

    User deleteFriend(Integer user, Integer friendsId);

    Boolean isUserExists(Integer filmId);

    void clear();
}
