package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dataBase.GenreDb;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    GenreDb genreDb;

    public List<Genre> getGenres() {
        return genreDb.findAll();
    }

    public Genre getGenreById(Integer genreId) {
        return genreDb.findById(genreId).get();
    }

}
