package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User newUser);

    Map<Integer, User> getUsers();

    void clear();
}
