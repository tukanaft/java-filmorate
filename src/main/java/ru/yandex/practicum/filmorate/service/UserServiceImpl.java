package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepositoryImpl userRepository;

    @Override
    public User addUser(User newUser) {
        log.info("UserService: выполнение запроса на добавление пользователя: {}", newUser);
        validateUser(newUser);
        return userRepository.addUser(newUser);
    }

    @Override
    public User updateUser(User newUser, Integer id) {
        log.info("UserService: выполнение запроса на обновление пользователя: {}", newUser);
        validateUser(newUser);
        return userRepository.updateUser(newUser, id);
    }

    @Override
    public List<User> getUsers() {
        log.info("UserService: выполнение запроса на получение пользователей");
        return userRepository.getUsers();
    }

    public void clear(){
        userRepository.clear();
    }

    private void validateUser(User user) {
        log.info("UserService: выполнение вальдации");
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        String name = user.getName();

        if (birthday == null || birthday.isAfter(LocalDate.now())){
            log.info("UserService: валидация даты рождения пользователя не пройдена: {}", birthday);
            throw new ValidationException("не корректная дата рождения");
        }
        if (email == null || !email.contains("@")){
            log.info("UserService: валидация email  пользователя не пройдена: {}", email);
            throw new ValidationException("не корректный email");
        }
        if (login == null || login.contains(" ")){
            log.info("UserService: валидация логина пользователя не пройдена: {}", login);
            throw new ValidationException("не корректный логин");
        }
        if (name == null || name.isEmpty()){
            user.setName(user.getLogin());
        }
    }
}
