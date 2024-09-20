package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserRepository {
    User addUser(User newUser);

    User updateUser(User newUser);

    ArrayList<User> getUsers();

    void clear();
}
