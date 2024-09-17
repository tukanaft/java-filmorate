package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User newUser);

    List<User> getUsers();

    void clear();
}
