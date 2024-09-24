package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

public interface FilmService {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    ArrayList<Film> getFilms();

    Boolean like(Integer filmId, Integer userId);

    Boolean unlike(Integer filmId, Integer userId);

    List<Film> mostPopularFilms(Integer count);

    void clear();
}
