package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private ArrayList<User> commonFriendsList;
    private final UserStorage userStorage;

    @Override
    public User addUser(User newUser) {
        log.info("UserService: выполнение запроса на добавление пользователя: {}", newUser);
        validateUser(newUser);
        return userStorage.addUser(newUser);
    }

    @Override
    public User updateUser(User newUser) {
        log.info("UserService: выполнение запроса на обновление пользователя: {}", newUser);
        validateUser(newUser);
        return userStorage.updateUser(newUser);
    }

    @Override
    public ArrayList<User> getUsers() {
        log.info("UserService: выполнение запроса на получение пользователей");
        return new ArrayList<>(userStorage.getUsers().values());
    }

    @Override
    public User addFriend(Integer userId, Integer friendsId) {
        log.info("UserService: выполнение запроса на добовление друга");
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!userStorage.isUserExists(friendsId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!(userStorage.getUsers().get(userId).getFriendsId() == null)) {
            if (userStorage.getUsers().get(userId).getFriendsId().contains(friendsId)) {
                throw new RuntimeException("такой друг уже добавлен");
            }
        }
        return userStorage.addFriend(userId, friendsId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendsId) {
        log.info("UserService: выполнение запроса на удаление друга");
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!userStorage.isUserExists(friendsId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!userStorage.getUsers().get(userId).getFriendsId().contains(friendsId)) {
            throw new RuntimeException("этот пользователь не ваш друг");
        }
        return userStorage.deleteFriend(userId, friendsId);
    }

    @Override
    public ArrayList<User> commonFriends(Integer userId, Integer friendsId) {
        log.info("UserService: выполнение запроса на поиск общих друзей");
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!userStorage.isUserExists(friendsId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendsId);
        commonFriendsList = new ArrayList<>();
        if (user.getFriendsId() == null) {
            throw new ValidationException("У вас нет друзей");
        }
        if (friend.getFriendsId() == null) {
            throw new ValidationException("У человека с которым вы пытаетесь найти общих друзей нет друзей");
        }
        for (Integer usersFriendId : user.getFriendsId()) {
            if (friend.getFriendsId().contains(usersFriendId)) {
                commonFriendsList.add(userStorage.getUsers().get(usersFriendId));
            }
        }
        return commonFriendsList;
    }

    @Override
    public void clear() {
        userStorage.clear();
    }

    @Override
    public ArrayList<User> getFriends(Integer userId) {
        log.info("UserService: выполнение запроса на отправление списка друзей пользователя");
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return userStorage.getFriends(userId);
    }

    private void validateUser(User user) {
        log.info("UserService: выполнение вальдации");
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        String name = user.getName();

        if (birthday == null || birthday.isAfter(LocalDate.now())) {
            log.info("UserService: валидация даты рождения пользователя не пройдена: {}", birthday);
            throw new ValidationException("не корректная дата рождения");
        }
        if (email == null || !email.contains("@")) {
            log.info("UserService: валидация email  пользователя не пройдена: {}", email);
            throw new ValidationException("не корректный email");
        }
        if (login == null || login.contains(" ")) {
            log.info("UserService: валидация логина пользователя не пройдена: {}", login);
            throw new ValidationException("не корректный логин");
        }
        if (name == null || name.isEmpty()) {
            user.setName(user.getLogin());
        }
    }


}
