package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component

public class FilmRepositoryImpl implements FilmRepository {
    private HashMap<Integer, Film> films;
    private Integer filmId;

    public FilmRepositoryImpl() {
        films = new HashMap<>();
        filmId = 1;
    }

    public Film addFilm(Film newFilm) {
        newFilm.setId(filmId);
        filmId++;
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film updateFilm(Film newFilm) {
        int foundFilm = 0;
        if (films.containsKey(newFilm.getId())) {
            films.get(newFilm.getId()).setName(newFilm.getName());
            films.get(newFilm.getId()).setDescription(newFilm.getDescription());
            films.get(newFilm.getId()).setReleaseDate(newFilm.getReleaseDate());
            films.get(newFilm.getId()).setDuration((newFilm.getDuration()));
            foundFilm = 1;
        }

        if (foundFilm == 0) {
            throw new IllegalArgumentException("пользователь не найден");
        }
        return newFilm;
    }

    public ArrayList<Film> getFilms() {
        return (ArrayList<Film>) films.values();
    }

    public void clear() {
        films.clear();
    }

}

