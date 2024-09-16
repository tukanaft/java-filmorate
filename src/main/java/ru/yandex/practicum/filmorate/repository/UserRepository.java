package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User newUser);
    User updateUser(User newUser, Integer id);
    List<User> getUsers();
    void clear();
}
