package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
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
        return filmService.addFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        log.info("FilmController: выполнение запроса на обновление фильма: {}", newFilm);
        return filmService.updateFilm(newFilm);
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        log.info("FilmController: выполнение запроса на получение фильма");
        return filmService.getFilms();
    }
    @PutMapping("/{id}/like/{userId}")
    public Film like(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("FilmController: выполнение запроса на добавление лайка: {}", filmId);
        return filmService.like(filmId,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film unlike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("FilmController: выполнение запроса на удаление лайка: {}", filmId);
        return filmService.like(filmId,userId);
    }

    @GetMapping("films/popular?count={count}")
    public List<Film> commonFriends(@PathVariable("count") Integer count){
        log.info("FilmController: выполнение запроса на получение самых популярных фильмов");
        return filmService.mostPopularFilms(count);
    }
}
