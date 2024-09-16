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
    void clear(){
        filmService.clear();
    }

    @Test
    void whenAddFilmIsSucsess() {
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
    void whenAddFilmWithInvalidNameIsNotSucsess() {
        Film film = Film.builder()
                .name("")
                .description("Description of film")
                .duration(Duration.ofHours(1))
                .releaseDate(LocalDate.now())
                .build();
        Assertions.assertThatThrownBy(()->filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("название фильма не введено");
    }

    @Test
    void updateFilm() {
    }

}