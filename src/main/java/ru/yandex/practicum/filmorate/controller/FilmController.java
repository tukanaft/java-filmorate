package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;
import ru.yandex.practicum.filmorate.exception.BadInputExceptionParametered;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto findFilmById(@PathVariable Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmRequest filmRequest) {
        return filmService.create(filmRequest);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmRequest filmRequest) {
        return filmService.update(filmRequest);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable Long filmId) {
        filmService.delete(filmId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getFilmsTop(@RequestParam(defaultValue = "10") int count,
                                     @RequestParam(defaultValue = "0") Long genreId,
                                     @RequestParam(defaultValue = "0") Integer year) {
        if (count < 1) {
            throw new BadInputExceptionParametered("count", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        LocalDate date = LocalDate.ofYearDay(year, 1);
        if (genreId == 0 && year == 0) {
            return filmService.getTopFilms(count);
        } else {
            return filmService.getTopFilmsByGenreYear(count, genreId, date);
        }
    }

    @PutMapping("/{filmId}/like/{userId}")
    public boolean putLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.putLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public boolean deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.deleteLike(filmId, userId);
    }


    @GetMapping("/director/{directorId}")
    public List<FilmDto> getDirectorFilmsByYears(@RequestParam String sortBy, @PathVariable Long directorId) {
        if (sortBy.equals("year")) {
            return filmService.getDirectorsFilmsByYear(directorId);
        }
        if (sortBy.equals("likes")) {
            return filmService.getDirectorsFilmsByLikes(directorId);
        } else {
            throw new BadInputExceptionParametered("error", "Некорректный запрос");
        }
    }

    @GetMapping("/common")
    public Collection<FilmDto> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("Получен запрос на вывод общих фильмов для пользователя id={} и пользователя с id={}", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<FilmDto> searchFilms(@RequestParam("query") String query, @RequestParam("by") String by) {
        return filmService.searchFilms(query, by);
    }
}
