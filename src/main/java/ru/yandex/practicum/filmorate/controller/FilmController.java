package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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

    @PutMapping("/{id}")
    public Film updateFilm(@RequestBody Film newFilm, @PathVariable("id") Integer id) {
        log.info("FilmController: выполнение запроса на обновление фильма: {}", newFilm);
        return filmService.updateFilm(newFilm, id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("FilmController: выполнение запроса на получение фильма");
        return filmService.getFilms();
    }
}
