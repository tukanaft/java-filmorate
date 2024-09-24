package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component

public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films;
    private Integer filmId;

    public InMemoryFilmStorage() {
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
            throw new NotFoundException("фильм не найден");
        }
        return newFilm;
    }

    public HashMap<Integer, Film> getFilms() {
        return new HashMap<Integer, Film>(films);
    }

    public Film like(Integer filmId, Integer userId) {
        Integer[] array = {userId};
        ArrayList<Integer> firstLike = new ArrayList<Integer>(Arrays.asList(array));
        Film film = films.get(filmId);
        if (film.getLikes().contains(userId)) {
            return films.get(filmId);
        }
        if (film.getLikes() == null) {
            film.setLikes(firstLike);
        } else {
            films.get(filmId).getLikes().add(userId);
        }
        return films.get(filmId);
    }

    public Film unlike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().remove(userId);
        return films.get(filmId);
    }


    public Boolean isFilmExists(Integer filmId) {
        return films.containsKey(filmId);
    }

    public void clear() {
        films.clear();
    }

}

