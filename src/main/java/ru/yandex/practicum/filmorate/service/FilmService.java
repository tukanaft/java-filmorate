package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.ArrayList;
import java.util.List;

public interface FilmService {
    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    ArrayList<FilmDto> getFilms();

    Film getFilm(Integer filmId);

    Boolean like(Integer filmId, Integer userId);

    Boolean unlike(Integer filmId, Integer userId);

    List<FilmDto> mostPopularFilms(Integer count);

    void clear();
}
