package ru.yandex.practicum.filmorate.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @AfterEach
    void clear() {
        userService.clear();
    }

    @Test
    void whenAddUserIsSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User actual = userService.getUsers().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(user.getName());
        Assertions.assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(actual.getLogin()).isEqualTo(user.getLogin());
        Assertions.assertThat(actual.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    void whenAddUserWithInvalidBirthdayIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2040, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата рождения");
    }

    @Test
    void whenAddUserWithInvalidLoginIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректный логин");
    }

    @Test
    void whenAddUserWithInvalidEmailIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("emailyandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректный email");
    }

    @Test
    void whenUpdateUserIsSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User userUpd = User.builder()
                .id(1)
                .name("nameUpd")
                .email("email2@yandex.ru")
                .login("loginUpd")
                .birthday(LocalDate.ofYearDay(2020, 20))
                .build();
        userService.updateUser(userUpd);
        User actual = userService.getUsers().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(user.getName());
        Assertions.assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(actual.getLogin()).isEqualTo(user.getLogin());
        Assertions.assertThat(actual.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    void whenUpdateUserWithInvalidBirthdayIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User userUpd = User.builder()
                .id(1)
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2040, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.updateUser(userUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата рождения");
    }

    @Test
    void whenUpdateUserWithInvalidEmailIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User userUpd = User.builder()
                .id(1)
                .name("name")
                .email("email2yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.updateUser(userUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректный email");
    }

    @Test
    void whenUpdateUserWithInvalidLoginIsNotSuccess() {
        User user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User userUpd = User.builder()
                .id(1)
                .name("name")
                .email("email@yandex.ru")
                .login("login login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> userService.updateUser(userUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректный логин");
    }

    @Test
    void whenAddFriendIsSuccess() {
        User user = User.builder()
                .id(4)
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User friend = User.builder()
                .id(5)
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(friend);
        userService.addFriend(user.getId(), friend.getId());
        User actual = userService.getUsers().get(1);
        Assertions.assertThat(actual.getId()).isEqualTo(friend.getId());
        Assertions.assertThat(actual.getName()).isEqualTo(friend.getName());
        Assertions.assertThat(actual.getEmail()).isEqualTo(friend.getEmail());
        Assertions.assertThat(actual.getLogin()).isEqualTo(friend.getLogin());
        Assertions.assertThat(actual.getBirthday()).isEqualTo(friend.getBirthday());
    }

    @Test
    void whenCommonFriendIsSuccess() {
        User user = User.builder()
                .id(19)
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User otherUser = User.builder()
                .id(20)
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(otherUser);
        userService.addFriend(19, 20);
        ArrayList<User> friends = new ArrayList<User>(userService.commonFriends(user.getId(), otherUser.getId()));
        User actual = friends.get(0);
        Assertions.assertThat(actual.getId()).isEqualTo(otherUser.getId());
        Assertions.assertThat(actual.getName()).isEqualTo(otherUser.getName());
        Assertions.assertThat(actual.getEmail()).isEqualTo(otherUser.getEmail());
        Assertions.assertThat(actual.getLogin()).isEqualTo(otherUser.getLogin());
        Assertions.assertThat(actual.getBirthday()).isEqualTo(otherUser.getBirthday());
    }
}