package ru.yandex.practicum.filmorate.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;


@SpringBootTest
class FilmServiceImplTest {
    @Autowired
    private FilmService filmService;

    @AfterEach
    void clear() {
        filmService.clear();
    }

    @Test
    void whenAddFilmIsSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film actual = filmService.getFilms().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(film.getName());
        Assertions.assertThat(actual.getDescription()).isEqualTo(film.getDescription());
        Assertions.assertThat(actual.getDuration()).isEqualTo(film.getDuration());
        Assertions.assertThat(actual.getReleaseDate()).isEqualTo(film.getReleaseDate());
    }

    @Test
    void whenAddFilmWithInvalidNameIsNotSuccess() {
        Film film = Film.builder()
                .name("")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("название фильма не введено");
    }

    @Test
    void whenAddFilmWithInvalidDescriptionIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Кстати, реплицированные с зарубежных источников, современные исследования освещают чрезвычайно интересные особенности картины в целом, однако конкретные выводы, разумеется, функционально разнесены на независимые элементы! Внезапно, непосредственные участники технического прогресса будут представлены в исключительно положительном свете. Однозначно, непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть смешаны с не уникальными данными до степени совершенной неузнаваемости, из-за чего возрастает их статус бесполезности.")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не корректное описание");
    }

    @Test
    void whenAddFilmWithInvalidDurationIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(-11))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не корректная длительность фильма");
    }

    @Test
    void whenAddFilmWithInvalidReleaseDateIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.ofYearDay(1000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата релиза");
    }

    @Test
    void whenUpdateFilmIsSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("nameUpd")
                .description("Description of filmUpd")
                .duration(Duration.ofHours(2))
                .releaseDate(LocalDate.now())
                .build();

    }

    @Test
    void whenUpdateFilmWithInvalidNameIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("")
                .description("Description of filmUpd")
                .duration(Duration.ofHours(2))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(filmUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("название фильма не введено");
    }

    @Test
    void whenUpdateFilmWithInvalidDescriptionIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Кстати, реплицированные с зарубежных источников, современные исследования освещают чрезвычайно интересные особенности картины в целом, однако конкретные выводы, разумеется, функционально разнесены на независимые элементы! Внезапно, непосредственные участники технического прогресса будут представлены в исключительно положительном свете. Однозначно, непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть смешаны с не уникальными данными до степени совершенной неузнаваемости, из-за чего возрастает их статус бесполезности.")
                .duration(Duration.ofHours(2))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(filmUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не корректное описание");
    }

    @Test
    void whenUpdateFilmWithInvalidDurationIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(-11))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(filmUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не корректная длительность фильма");
    }

    @Test
    void whenUpdateFilmWithInvalidReleaseDateIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.ofYearDay(1000, 20))
                .build();
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(filmUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата релиза");
    }
}