package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    List<Film> getFilms();

    void clear();
}
