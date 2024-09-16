package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film newFilm);
    Film updateFilm(Film newFilm, Integer id);
    List<Film> getFilms();
    void clear();
}
