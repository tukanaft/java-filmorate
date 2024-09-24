package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component

public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users;
    private Integer userId;
    private Integer[] array = {1};
    private ArrayList<Integer> firstFriend;
    private ArrayList<Integer> friendsFirstFriend;

    public InMemoryUserStorage() {
        users = new HashMap<>();
        userId = 1;
        firstFriend = new ArrayList<Integer>(Arrays.asList(array));
        friendsFirstFriend = new ArrayList<Integer>(Arrays.asList(array));
    }

    public User addUser(User newUser) {
        if (newUser.getId() == null) {
            newUser.setId(userId++);
        }
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
            throw new NotFoundException(
                    "пользователь не найден");
        }
        return newUser;
    }

    public HashMap<Integer, User> getUsers() {
        return new HashMap<Integer, User>(users);
    }

    public User addFriend(Integer userId, Integer friendsId) {
        User user = users.get(userId);
        User friend = users.get(friendsId);
        if (user.getFriendsId() == null) {
            firstFriend.clear();
            firstFriend.add(friendsId);
            user.setFriendsId(firstFriend);
        } else {
            user.getFriendsId().add(friendsId);
        }
        if (friend.getFriendsId() == null) {
            friendsFirstFriend.clear();
            friendsFirstFriend.add(userId);
            friend.setFriendsId(friendsFirstFriend);
        } else {
            friend.getFriendsId().add(userId);
        }
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendsId) {
        users.get(userId).getFriendsId().remove(friendsId);
        return users.get(userId);
    }

    public Boolean isUserExists(Integer userId) {
        return users.containsKey(userId);
    }

    public void clear() {
        users.clear();
    }
}