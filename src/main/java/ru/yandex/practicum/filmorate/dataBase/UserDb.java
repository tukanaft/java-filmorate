package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.User;

public interface UserDb extends JpaRepository<User, Integer> {
}
