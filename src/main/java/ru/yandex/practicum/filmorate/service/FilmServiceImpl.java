package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film addFilm(Film newFilm) {
        log.info("FilmService: выполнение запроса на добавление фильма: {}", newFilm);
        validateFilm(newFilm);
        return filmStorage.addFilm(newFilm);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("FilmService: выполнение запроса на обновление фильма: {}", newFilm);
        validateFilm(newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    @Override
    public ArrayList<Film> getFilms() {
        log.info("FilmService: выполнение запроса на получение фильма");
        return new ArrayList<Film>(filmStorage.getFilms().values());
    }

    @Override
    public Film like(Integer filmId, Integer userId) {
        log.info("FilmService: выполнение запроса на добавление лайка");
        if (!filmStorage.isFilmExists(filmId)) {
            throw new NotFoundException("Такого фильма нет в базе");
        }
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
            throw new ValidationException("вы уже лайкали этот фильм");
        }
        return filmStorage.like(filmId, userId);
    }

    @Override
    public Film unlike(Integer filmId, Integer userId) {
        log.info("FilmService: выполнение запроса на удаление лайка");
        if (!filmStorage.isFilmExists(filmId)) {
            throw new NotFoundException("Такого фильма нет в базе");
        }
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
            throw new ValidationException("вы еще не лайкали этот фильм");
        }
        return filmStorage.unlike(filmId, userId);
    }

    @Override
    public List<Film> mostPopularFilms(Integer count) {
        log.info("FilmService: выполнение запроса на получение самых популярных фильмов");
        if (count == null) {
            count = 10;
        }
        Comparator<Film> comparator = (film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size());
        List<Film> films = getFilms();
        return films.stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());

    }

    @Override
    public void clear() {
        filmStorage.clear();
    }

    private void validateFilm(Film film) {
        log.info("FilmService: начало выполнение валидации фильма: {}", film);
        String filmName = film.getName();
        String filmDescription = film.getDescription();
        LocalDate filmDate = film.getReleaseDate();
        Integer filmduration = film.getDuration();


        if (filmName == null || filmName.isEmpty()) {
            log.info("FilmService: валидация названия фильма не пройдена: {}", filmName);
            throw new ValidationException("название фильма не введено");
        }
        if (filmDescription == null || filmDescription.length() > 200) {
            log.info("FilmService: валидации описания фильма не пройдена: {}", filmDescription);
            throw new ValidationException("Не корректное описание");
        }
        if (filmDate == null || filmDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("FilmService: валидации даты создания фильма не пройдена: {}", filmDate);
            throw new ValidationException("не корректная дата релиза");
        }
        if (filmduration == null || filmduration <= 0) {
            log.info("FilmService: валидации продолжительности фильма не пройдена: {}", filmduration);
            throw new ValidationException("Не корректная длительность фильма");
        }
    }

}
