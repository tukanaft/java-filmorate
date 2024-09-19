package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmRepository {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    ArrayList<Film> getFilms();

    void clear();
}
