package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.exception.BadInputException;
import ru.yandex.practicum.filmorate.exception.BadInputExceptionParametered;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final EventService eventService;

    @Override
    public List<FilmDto> getTopFilms(int count) {
        return filmRepository.getTopFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> getTopFilmsByGenreYear(int count, long genreId, LocalDate date) {
        return filmRepository.getTopFilmsByGenreYear(count, genreId, date).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto findFilmById(Long filmId) {
        return filmRepository.findById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
    }

    @Override
    public boolean putLike(Long id, Long userId) {
        checkFilmId(filmRepository, id);
        checkUserId(userRepository, userId);
        eventService.addEvent(new Event(userId, EventType.LIKE, OperationType.ADD, id, Instant.now().toEpochMilli()));
        return filmRepository.putLike(id, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        checkFilmId(filmRepository, filmId);
        checkUserId(userRepository, userId);
        eventService.addEvent(new Event(userId, EventType.LIKE, OperationType.REMOVE, filmId, Instant.now().toEpochMilli()));
        return filmRepository.deleteLike(filmId, userId);
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto create(FilmRequest filmRequest) {
        Film film = FilmMapper.mapToFilm(filmRequest);
        addRating(film, filmRequest);
        film = filmRepository.save(film);
        addGenres(film, filmRequest);
        addDirector(film, filmRequest);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public boolean delete(Long id) {
        checkFilmId(filmRepository, id);
        return filmRepository.delete(id);
    }

    @Override
    public FilmDto update(FilmRequest filmRequest) {
        if (filmRequest.getId() == null) {
            throw new BadInputExceptionParametered("ID", "ID фильма не может быть пустым.");
        }

        Film updatedFilm = filmRepository.findById(filmRequest.getId())
                .map(film -> FilmMapper.updateFilmFields(film, filmRequest))
                .orElseThrow(() -> new NotFoundException(filmRequest.getId() + " фильм с таким ИД не найден"));
        addRating(updatedFilm, filmRequest);
        addGenres(updatedFilm, filmRequest);
        addDirector(updatedFilm, filmRequest);
        updatedFilm = filmRepository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    @Override
    public Collection<FilmDto> getCommonFilms(Long userId, Long friendId) {
        Optional<User> optUser = userRepository.findById(userId);
        Optional<User> optFriend = userRepository.findById(friendId);
        if (optUser.isEmpty() || optFriend.isEmpty()) {
            log.error("Пользователь с id {} или с id {} не добавлен", userId, friendId);
            throw new NotFoundException(String.format("Пользователь с id {} или с id {} не добавлен", userId, friendId));
        }
        Collection<Film> commonFilms = filmRepository.getCommonFilms(userId, friendId);
        return commonFilms.stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public void checkFilmId(FilmRepository filmRepository, Long... ids) {
        for (Long id : ids) {
            if (filmRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Фильма с ID " + id + " не существует");
            }
        }
    }

    public void checkUserId(UserRepository userRepository, Long... ids) {
        for (Long id : ids) {
            if (userRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Юзера с ID " + id + " не существует");
            }
        }
    }

    private void addRating(Film film, FilmRequest filmRequest) {
        Long ratingId = filmRequest.getMpa().getId();
        Rating rating = ratingRepository.get(ratingId)
                .orElseThrow(() -> new BadInputExceptionParametered("ID", "Рейтинг с ID " + ratingId + " не найден"));
        film.setMpa(rating);
    }

    private void addGenres(Film film, FilmRequest filmRequest) {
        filmRepository.deleteAllGenresForFilm(film.getId());
        if (filmRequest.getGenres() == null) {
            film.setGenres(new HashSet<>());
            return;
        }
        Set<Genre> genres = filmRequest.getGenres().stream()
                .map(GenreRequest::getId)
                .map(genreRepository::findById)
                .map(genre -> genre
                        .orElseThrow(() -> new BadInputException("Жанр не найден")))
                .peek(genre -> filmRepository.addGenreForFilm(film.getId(), genre.getId()))
                .collect(Collectors.toSet());
        film.setGenres(genres);
    }

    private void addDirector(Film film, FilmRequest filmRequest) {
        filmRepository.deleteAllDirectorsForFilm(film.getId());
        if (filmRequest.getDirectors() == null) {
            film.setDirectors(new HashSet<>());
            return;
        }

        Set<Director> directors = filmRequest.getDirectors().stream()
                .map(DirectorRequest::getId)
                .map(directorRepository::findById)
                .map(director -> director
                        .orElseThrow(() -> new NotFoundException("Режиссер не найден")))
                .peek(director -> filmRepository.addDirectorForFilm(film.getId(), director.getId()))
                .collect(Collectors.toSet());
        film.setDirectors(directors);
    }

    public void checkDirectorId(DirectorRepository directorRepository, Long... ids) {
        for (Long id : ids) {
            if (directorRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Директора с ID " + id + " не существует");
            }
        }
    }

    @Override
    public List<FilmDto> getDirectorsFilmsByYear(Long id) {
        checkDirectorId(directorRepository, id);
        return filmRepository.getDirectorsFilmSortByYear(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> getDirectorsFilmsByLikes(Long id) {
        checkDirectorId(directorRepository, id);
        return filmRepository.getDirectorsFilmSortByLikes(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> searchByFilm(String query) {
        return filmRepository.searchByFilm(query.toLowerCase()).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> searchByDirector(String query) {
        return filmRepository.searchByDirector(query.toLowerCase()).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> searchFilms(String query, String by) {
        List<FilmDto> results;

        switch (by) {
            case "title" -> results = searchByFilm(query);
            case "director" -> results = searchByDirector(query);
            case "title,director", "director,title" -> {
                results = new ArrayList<>(searchByDirector(query));
                results.addAll(searchByFilm(query));
                return results.stream().distinct().collect(Collectors.toList());
            }
            default -> throw new BadInputException("Поиск должен быть по director или title");
        }

        return results;
    }
}
