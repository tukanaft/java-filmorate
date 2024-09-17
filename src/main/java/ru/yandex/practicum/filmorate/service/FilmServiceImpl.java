package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepositoryImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepositoryImpl filmRepository;

    @Override
    public Film addFilm(Film newFilm) {
        log.info("FilmService: выполнение запроса на добавление фильма: {}", newFilm);
        validateFilm(newFilm);
        return filmRepository.addFilm(newFilm);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("FilmService: выполнение запроса на обновление фильма: {}", newFilm);
        validateFilm(newFilm);
        return filmRepository.updateFilm(newFilm);
    }

    @Override
    public List<Film> getFilms() {
        log.info("FilmService: выполнение запроса на получение фильма");
        return filmRepository.getFilms();
    }

    @Override
    public void clear() {
        filmRepository.clear();
    }

    private void validateFilm(Film film) {
        log.info("FilmService: начало выполнение валидации фильма: {}", film);
        String filmName = film.getName();
        String filmDescription = film.getDescription();
        LocalDate filmDate = film.getReleaseDate();
        Duration filmduration = film.getDuration();


        if (filmName == null || filmName.isEmpty()) {
            log.info("FilmService: валидация названия фильма не пройдена: {}", filmName);
            throw new ValidationException("название фильма не введено");
        }
        if (filmDescription == null || filmDescription.length() > 200 || filmDescription.length() < 5) {
            log.info("FilmService: валидации описания фильма не пройдена: {}", filmDescription);
            throw new ValidationException("Не корректное описание");
        }
        if (filmDate == null || filmDate.isBefore(LocalDate.of(1885, 12, 28))) {
            log.info("FilmService: валидации даты создания фильма не пройдена: {}", filmDate);
            throw new ValidationException("не корректная дата релиза");
        }
        if (filmduration == null || filmduration.isNegative()) {
            log.info("FilmService: валидации продолжительности фильма не пройдена: {}", filmduration);
            throw new ValidationException("Не корректная длительность фильма");
        }
    }

}
