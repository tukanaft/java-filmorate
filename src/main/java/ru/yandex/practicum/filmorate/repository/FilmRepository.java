package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmRepository {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Map<Integer, Film> getFilms();

    void clear();
}
