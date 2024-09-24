package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component

public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users;
    private Integer userId;


    public InMemoryUserStorage() {
        users = new HashMap<>();
        userId = 1;
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
        Integer[] array = {1};
        ArrayList<Integer> firstFriend = new ArrayList<Integer>(Arrays.asList(array));
        ArrayList<Integer> friendsFirstFriend = new ArrayList<Integer>(Arrays.asList(array));
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
            if (!friend.getFriendsId().contains(userId)) {
                friend.getFriendsId().add(userId);
            }
        }
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendsId) {
        if (!users.get(userId).getFriendsId().contains(friendsId)) {
            return users.get(userId);
        }
        users.get(userId).getFriendsId().remove(friendsId);
        users.get(friendsId).getFriendsId().remove(userId);
        return users.get(userId);
    }

    public ArrayList<User> getFriends(Integer userId) {
        ArrayList<User> friends = new ArrayList<>();
        User user = users.get(userId);
        if (!(user.getFriendsId() == null)) {
            for (Integer friendsId : user.getFriendsId()) {
                friends.add(users.get(friendsId));
            }
        }
        return friends;
    }

    public Boolean isUserExists(Integer userId) {
        return users.containsKey(userId);
    }

    public void clear() {
        users.clear();
    }
}