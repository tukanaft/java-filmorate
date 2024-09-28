package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Raiting;

public interface RaitingDb extends JpaRepository<Raiting, Integer> {
}
