package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserRepository {
    User addUser(User newUser);

    User updateUser(User newUser);

    Map<Integer, User> getUsers();

    void clear();
}
