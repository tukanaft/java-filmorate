package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {
    User addUser(User newUser);

    User updateUser(User newUser);

    HashMap<Integer,User> getUsers();

    User addFriend(Integer user, Integer friendsId);

    User deleteFriend(Integer user, Integer friendsId);

    Boolean isUserExists(Integer filmId);

    void clear();
}
