package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                    "пользователь не найден", newUser.getId());
        }
        return newUser;
    }

    public HashMap<Integer, User> getUsers() {
        return new HashMap<Integer, User>(users);
    }

    public Boolean addFriend(Integer userId, Integer friendsId) {
        HashMap<Integer, Status> firstFriend = new HashMap<Integer, Status>();
        firstFriend.put(friendsId, Status.UNAPPROVED);
        HashMap<Integer, Status> friendsFirstFriend = new HashMap<Integer, Status>();
        friendsFirstFriend.put(userId, Status.REQUESTED);
        User user = users.get(userId);
        User friend = users.get(friendsId);
        if (user.getFriendsId() == null) {
            user.setFriendsId(firstFriend);
        } else {
            if (user.getFriendsId().containsKey(friendsId)) {
                if (user.getFriendsId().get(friendsId).equals(Status.REQUESTED)) {
                    user.getFriendsId().replace(friendsId, Status.APPROVED);
                    friend.getFriendsId().replace(userId, Status.APPROVED);
                }
            } else {
                user.getFriendsId().put(friendsId, Status.UNAPPROVED);
            }
        }
        if (friend.getFriendsId() == null) {
            friend.setFriendsId(friendsFirstFriend);
        } else {
            if (!friend.getFriendsId().containsKey(userId)) {
                friend.getFriendsId().put(userId, Status.REQUESTED);
            }
        }
        return true;
    }

    public Boolean deleteFriend(Integer userId, Integer friendsId) {
        if (users.get(userId).getFriendsId() == null) {
            return false;
        }
        users.get(userId).getFriendsId().remove(friendsId);
        users.get(friendsId).getFriendsId().remove(userId);
        return true;
    }

    public List<User> getFriends(Integer userId) {
        ArrayList<User> friends = new ArrayList<>();
        User user = users.get(userId);
        if (!(user.getFriendsId() == null)) {
            for (Integer friendsId : user.getFriendsId().keySet()) {
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