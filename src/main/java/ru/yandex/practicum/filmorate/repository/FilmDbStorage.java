package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dataBase.FilmDb;
import ru.yandex.practicum.filmorate.dataBase.LikeDb;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.HashMap;

@Component
public class FilmDbStorage implements FilmStorage {

    @Autowired
    FilmDb filmDb;
    @Autowired
    LikeDb likeDb;

    @Override
    public Film addFilm(Film newFilm) {
        filmDb.save(newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (isFilmExists(newFilm.getId())) {
            filmDb.save(newFilm);
        } else {
            throw new NotFoundException("фильм который вы пытаетесь обновить не существует", newFilm.getId());
        }
        return newFilm;
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        HashMap<Integer, Film> filmsUpload = new HashMap<>();
        for (Film film : filmDb.findAll()) {
            filmsUpload.put(film.getId(), film);
        }
        return filmsUpload;
    }

    @Override
    public Boolean like(Integer filmId, Integer userId) {
        Like likeObj = new Like();
        likeObj.setFilmId(filmId);
        likeObj.setUserId(userId);
        for (Film film : filmDb.findAll()) {
            if (film.getId().equals(filmId)) {
                if (film.getLikes().contains(userId)) {
                    return false;
                }
                film.getLikes().add(userId);
                likeDb.save(likeObj);
            }
        }
        return true;
    }

    @Override
    public Boolean unlike(Integer filmId, Integer userId) {
        for (Like likeObj : likeDb.findAll()) {
            if (likeObj.getFilmId().equals(filmId) && likeObj.getUserId().equals(userId)) {
                likeDb.delete(likeObj);
                return true;
            }
        }
        return false;
    }


    @Override
    public Boolean isFilmExists(Integer filmId) {
        for (Film film : filmDb.findAll()) {
            if (film.getId().equals(filmId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {

    }
}
