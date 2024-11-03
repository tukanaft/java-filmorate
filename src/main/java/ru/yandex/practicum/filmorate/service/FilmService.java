package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public interface FilmService {
    List<FilmDto> getTopFilms(int count);

    List<FilmDto> getTopFilmsByGenreYear(int count, long genreId, LocalDate date);

    FilmDto findFilmById(Long filmId);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    Collection<FilmDto> findAll();

    FilmDto create(FilmRequest filmRequest);

    FilmDto update(FilmRequest filmRequest);

    boolean delete(Long id);

    List<FilmDto> getDirectorsFilmsByYear(Long id);

    List<FilmDto> getDirectorsFilmsByLikes(Long id);

    Collection<FilmDto> getCommonFilms(Long userId, Long friendId);

    List<FilmDto> searchByFilm(String query);

    List<FilmDto> searchByDirector(String query);

    List<FilmDto> searchFilms(String query, String by);

}
