package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User newUser);

    User updateUser(User newUser);

    List<User> getUsers();

    void clear();
}
