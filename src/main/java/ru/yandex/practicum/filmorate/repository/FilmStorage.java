package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    HashMap<Integer, Film> getFilms();

    Boolean like(Integer filmId, Integer userId);

    Boolean unlike(Integer filmId, Integer userId);

    Boolean isFilmExists(Integer filmId);

    void clear();
}
