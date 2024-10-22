package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;

import java.util.Collection;
import java.util.List;

@Service
public interface FilmService {
    List<FilmDto> getTopFilms(int size);

    FilmDto findFilmById(Long filmId);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    Collection<FilmDto> findAll();

    FilmDto create(FilmRequest filmRequest);

    FilmDto update(FilmRequest filmRequest);

    boolean delete(Long id);
}
