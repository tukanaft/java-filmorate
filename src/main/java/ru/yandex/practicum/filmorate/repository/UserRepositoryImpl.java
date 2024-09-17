package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component

public class UserRepositoryImpl implements UserRepository {
    private List<User> users;
    private Integer userId;

    public UserRepositoryImpl() {
        users = new ArrayList<>();
        userId = 1;
    }

    public User addUser(User newUser) {
        newUser.setId(userId++);
        users.add(newUser);
        return newUser;
    }

    public User updateUser(User newUser) {
        User foundUser = null;
        for (User user : users) {
            if (Objects.equals(user.getId(), newUser.getId())) {
                foundUser = user;
            }
        }
        if (foundUser == null) {
            throw new IllegalArgumentException("пользователь не найден");
        }
        foundUser.setEmail(newUser.getEmail());
        foundUser.setLogin(newUser.getLogin());
        foundUser.setBirthday(newUser.getBirthday());
        return foundUser;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void clear() {
        users.clear();
    }
}