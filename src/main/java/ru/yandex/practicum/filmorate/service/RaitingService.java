package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.RatingDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaitingService {
    private final FilmDbStorage filmDbStorage;
    private final RatingDbStorage ratingDbStorage;

    public List<MPA> getRaitings() {
        log.info("GenreService: выполнение запроса на получение рейтингов");
        return ratingDbStorage.getRaitings();
    }

    public MPA getRaitingById(Integer raitingId) {
        log.info("GenreService: выполнение запроса на получение рейтинга по id: {}", raitingId);
        if (!filmDbStorage.ifMPAExists(raitingId)) {
            throw new NotFoundException("рейтинга не существует", raitingId);
        }
        return ratingDbStorage.getRaitingById(raitingId);
    }

}
