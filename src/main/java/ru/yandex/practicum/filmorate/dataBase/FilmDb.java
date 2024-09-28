package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDb extends JpaRepository<Film, Integer> {
}
