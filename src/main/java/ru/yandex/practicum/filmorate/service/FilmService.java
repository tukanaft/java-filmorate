package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmService {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    ArrayList<Film> getFilms();

    void clear();
}
