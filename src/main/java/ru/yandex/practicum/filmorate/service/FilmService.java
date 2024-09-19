package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmService {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Map<Integer, Film> getFilms();

    void clear();
}
