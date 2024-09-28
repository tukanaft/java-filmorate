package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Raiting;
import ru.yandex.practicum.filmorate.dataBase.FilmDb;

import java.time.LocalDate;

@Controller
public class FilmCreation {
    @Autowired
    private FilmDb filmDbRepo;

    ru.yandex.practicum.filmorate.model.Film film = new ru.yandex.practicum.filmorate.model.Film();
    public void addFilm(){
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000,2,2));
        film.setDuration(100);
        filmDbRepo.save(film);
    }
}
