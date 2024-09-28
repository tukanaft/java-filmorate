package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Friends;

public interface FriendsDb extends JpaRepository<Friends, Integer> {
}
