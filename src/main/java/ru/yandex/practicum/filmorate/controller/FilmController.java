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

    @GetMapping("/popular")
    public List<FilmDto> getFilmsTop(@RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "0") Long genreId,
                                     @RequestParam(defaultValue = "0") Integer year) {
        if (size < 1) {
            throw new BadInputExceptionParametered("size", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        LocalDate date = LocalDate.ofYearDay(year, 1);
        if (genreId == 0 && year == 0) {
            return filmService.getTopFilms(size);
        } else {
            return filmService.getTopFilmsByGenreYear(size, genreId, date);
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

}
