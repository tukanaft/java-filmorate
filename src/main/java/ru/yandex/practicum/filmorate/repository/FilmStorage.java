package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    ArrayList<Film> getFilms();

    Film like(Integer filmId, Integer userId);

    Film unlike(Integer filmId, Integer userId);

    Boolean isFilmExists(Integer filmId);

    void clear();
}
