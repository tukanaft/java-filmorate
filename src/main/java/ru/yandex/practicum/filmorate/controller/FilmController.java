package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film newFilm) {
        log.info("FilmController: выполнение запроса на добавление фильма: {}", newFilm);
        Film film = filmService.addFilm(newFilm);
        log.info("FilmController: запрос на добавление выполнен: {}",film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        log.info("FilmController: выполнение запроса на обновление фильма: {}", newFilm);
        return filmService.updateFilm(newFilm);
    }

    @GetMapping
    public ArrayList<FilmDto> getFilms() {
        log.info("FilmController: выполнение запроса на получение фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm (@PathVariable("id") Integer filmId){
        log.info("FilmController: выполнение запроса на получение фильма: {}", filmId);
        Film film = filmService.getFilm(filmId);
        log.info("FilmController: запрос на получение выполнен: {}",film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Boolean like(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("FilmController: выполнение запроса на добавление лайка: {}", filmId);
        return filmService.like(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Boolean unlike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("FilmController: выполнение запроса на удаление лайка: {}", filmId);
        return filmService.like(filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> commonFriends(@RequestParam Integer count) {
        log.info("FilmController: выполнение запроса на получение самых популярных фильмов");
        return filmService.mostPopularFilms(count);
    }

}

