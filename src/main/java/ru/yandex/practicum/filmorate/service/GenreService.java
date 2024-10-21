package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> getGenres() {
        log.info("GenreService: выполнение запроса на получение жанров");
        return genreDbStorage.getGenres();
    }

    public Genre getGenreById(Integer genreId) {
        log.info("GenreService: выполнение запроса на получение жанра по id: {}", genreId);
        if (genreId > 6 || genreId < 0) {
            throw new NotFoundException("не существующий жанр");
        }
        return genreDbStorage.getGenreById(genreId);
    }

}
