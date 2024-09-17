package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component

public class FilmRepositoryImpl implements FilmRepository {
    private List<Film> films;
    private Integer filmId;

    public FilmRepositoryImpl() {
        films = new ArrayList<>();
        filmId = 1;
    }

    public Film addFilm(Film newFilm) {
        newFilm.setId(filmId);
        filmId++;
        films.add(newFilm);
        return newFilm;
    }

    public Film updateFilm(Film newFilm) {
        Integer foundFilm = 0;
        for (Film film : films) {
            if (Objects.equals(film.getId(), newFilm.getId())) {
                newFilm.setName(newFilm.getName());
                newFilm.setDescription(newFilm.getDescription());
                newFilm.setReleaseDate(newFilm.getReleaseDate());
                newFilm.setDuration(newFilm.getDuration());
                foundFilm = 1;
            }
        }
        if (foundFilm == 0) {
            throw new IllegalArgumentException("пользователь не найден");
        }
        return newFilm;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films);
    }

    public void clear() {
        films.clear();
    }

}

