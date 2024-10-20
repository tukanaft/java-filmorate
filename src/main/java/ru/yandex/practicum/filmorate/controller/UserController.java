package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User newUser) {
        log.info("UserController: выполнение запроса на добавление пользователя: {}", newUser);
        User addedUser = userService.addUser(newUser);
        log.info("UserController: запрос на добавление пользователя выполнен: {}", addedUser);
        return addedUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        log.info("UserController: выполнение запроса на обновление пользователя: {}", newUser);
        User updatedUser = userService.updateUser(newUser);
        log.info("UserController: запрос на обновление пользователя выполнен: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping
    public ArrayList<User> getUsers() {
        log.info("UserController: выполнение запроса на получение пользователей");
        return userService.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Boolean addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendsId) {
        log.info("UserController: выполнение запроса на добавление друга: {}", userId);
        return userService.addFriend(userId, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Boolean deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendsId) {
        log.info("UserController: выполнение запроса на удаление друга: {}", userId);
        return userService.deleteFriend(userId, friendsId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer friendsId) {
        log.info("UserController: выполнение запроса на получение списка общих друзей: {}", userId);
        return userService.commonFriends(userId, friendsId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer userId) {
        log.info("UserController: выполнение запроса на отправление списка друзей пользователя: {}", userId);
        return userService.getFriends(userId);
    }
}
