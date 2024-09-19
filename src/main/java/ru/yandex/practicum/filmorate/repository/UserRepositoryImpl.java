package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component

public class UserRepositoryImpl implements UserRepository {
    private HashMap<Integer, User> users;
    private Integer userId;

    public UserRepositoryImpl() {
        users = new HashMap<>();
        userId = 1;
    }

    public User addUser(User newUser) {
        newUser.setId(userId++);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(User newUser) {
        Integer foundUser = 0;
        if (users.containsKey(newUser.getId())) {
            users.get(newUser.getId()).setEmail(newUser.getEmail());
            users.get(newUser.getId()).setLogin(newUser.getLogin());
            users.get(newUser.getId()).setBirthday(newUser.getBirthday());
            foundUser = 1;
        }
        if (foundUser == 0) {
            throw new IllegalArgumentException("пользователь не найден");
        }
        return newUser;
    }

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) users.values();
    }

    public void clear() {
        users.clear();
    }
}