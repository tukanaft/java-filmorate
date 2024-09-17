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
        newFilm.setId(filmId++);
        films.add(newFilm);
        return newFilm;
    }

    public Film updateFilm(Film newFilm) {
        Film foundFilm = null;
        for (Film film : films) {
            if (Objects.equals(film.getId(), newFilm.getId())) {
                foundFilm = film;

            }
        }
        if (foundFilm == null) {
            throw new IllegalArgumentException("пользователь не найден");
        }
        foundFilm.setName(newFilm.getName());
        foundFilm.setDescription(newFilm.getDescription());
        foundFilm.setReleaseDate(newFilm.getReleaseDate());
        foundFilm.setDuration(newFilm.getDuration());
        return foundFilm;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films);
    }

    public void clear() {
        films.clear();
    }

}

