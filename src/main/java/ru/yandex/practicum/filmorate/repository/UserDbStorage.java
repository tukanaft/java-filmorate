package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dataBase.FriendsDb;
import ru.yandex.practicum.filmorate.dataBase.UserDb;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class UserDbStorage implements UserStorage {

    @Autowired
    UserDb userDb;
    @Autowired
    FriendsDb friendsDb;

    @Override
    public User addUser(User newUser) {
        userDb.save(newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (isUserExists(newUser.getId())) {
            userDb.save(newUser);
        } else {
            throw new NotFoundException("такого пользователя не существует", newUser.getId());
        }
        return newUser;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> usersUpload = new HashMap<>();
        for (User user : userDb.findAll()) {
            usersUpload.put(user.getId(), user);
        }
        return usersUpload;
    }

    @Override
    public Boolean addFriend(Integer userId, Integer friendsId) {
        Friends userFriend = new Friends();
        Friends otherFriend = new Friends();
        User user = userDb.findById(userId).get();
        User friend = userDb.findById(friendsId).get();
        if (user.getFriendsId().containsKey(friendsId)) {
            for (Friends friends : friendsDb.findAll()) {
                if (friends.getUserid().equals(userId) && (friends.getFriend().equals(friendsId))) {
                    userFriend = friends;
                }
                else if (friends.getUserid().equals(friendsId) && (friends.getFriend().equals(userId))) {
                    otherFriend = friends;
                }
            }
        }
        if (user.getFriendsId().containsKey(friendsId)) {
            if (user.getFriendsId().get(friendsId).equals(Status.REQUESTED)) {
                user.getFriendsId().replace(friendsId, Status.APPROVED);
                friend.getFriendsId().replace(userId, Status.APPROVED);
                userDb.save(user);
                userFriend.setUserid(userId);
                userFriend.setFriend(friendsId);
                userFriend.setStatus(Status.APPROVED);
                friendsDb.save(userFriend);
                userDb.save(friend);
                otherFriend.setUserid(friendsId);
                otherFriend.setFriend(userId);
                otherFriend.setStatus(Status.APPROVED);
                friendsDb.save(otherFriend);
            }
        } else {
            user.getFriendsId().put(friendsId, Status.UNAPPROVED);
            userFriend.setUserid(userId);
            userFriend.setFriend(friendsId);
            userFriend.setStatus(Status.UNAPPROVED);
            friendsDb.save(userFriend);
            userDb.save(user);
            otherFriend.setUserid(userId);
            otherFriend.setFriend(friendsId);
            otherFriend.setStatus(Status.UNAPPROVED);
            friendsDb.save(otherFriend);
            userDb.save(friend);
        }
        return true;
    }

    @Override
    public Boolean deleteFriend(Integer userId, Integer friendsId) {
        User user = userDb.findById(userId).get();
        User friend = userDb.findById(friendsId).get();
        if (user.getFriendsId().containsKey(friendsId)) {
            return false;
        }
        if (user.getFriendsId().containsKey(friendsId)) {
            for (Friends friends : friendsDb.findAll()) {
                if (friends.getUserid().equals(userId) && (friends.getFriend().equals(friendsId))) {
                    friendsDb.delete(friends);
                }
                else if (friends.getUserid().equals(friendsId) && (friends.getFriend().equals(userId))) {
                    friendsDb.delete(friends);
                }
            }
        }
        user.getFriendsId().remove(friendsId);
        userDb.save(user);
        friend.getFriendsId().remove(userId);
        userDb.save(friend);
        return true;
    }

    @Override
    public Boolean isUserExists(Integer filmId) {
        for (User user : userDb.findAll()) {
            if (user.getId().equals(filmId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<User> getFriends(Integer userId) {
        return null;
    }

    @Override
    public void clear() {

    }
}
