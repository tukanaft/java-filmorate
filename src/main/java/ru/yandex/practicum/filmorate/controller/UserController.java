package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
        return userService.addUser(newUser);
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        log.info("UserController: выполнение запроса на обновление пользователя: {}", newUser);
        return userService.updateUser(newUser);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("UserController: выполнение запроса на получение пользователей");
        return userService.getUsers();
    }
}
