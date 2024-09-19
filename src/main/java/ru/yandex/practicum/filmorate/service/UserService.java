package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User newUser);

    ArrayList<User> getUsers();

    void clear();
}
