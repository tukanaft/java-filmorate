package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public List<MPA> getMpas() {
        log.info("GenreService: выполнение запроса на получение рейтингов");
        return mpaDbStorage.getMpas();
    }

    public MPA getMpaById(Integer mpaId) {
        log.info("GenreService: выполнение запроса на получение рейтинга по id: {}", mpaId);
        if (!filmDbStorage.ifMPAExists(mpaId)) {
            throw new NotFoundException("рейтинга не существует");
        }
        return mpaDbStorage.getMpaById(mpaId);
    }

}
