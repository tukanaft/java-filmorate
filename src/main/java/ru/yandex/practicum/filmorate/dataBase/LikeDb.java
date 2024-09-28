package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Like;

public interface LikeDb extends JpaRepository<Like, Integer> {
}
