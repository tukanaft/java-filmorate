package ru.yandex.practicum.filmorate.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Raiting;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;


import java.time.LocalDate;
import java.util.List;

@Disabled
@SpringBootTest
class FilmServiceImplTest {
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;

    @AfterEach
    void clear() {
        filmService.clear();
        userService.clear();
    }

    @Test
    void whenAddFilmIsSuccess() {
        Film film = Film.builder()
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        FilmDto actual = filmService.getFilms().get(0);
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
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(-11)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(1)
                .releaseDate(LocalDate.ofYearDay(1000, 20))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        Assertions.assertThatThrownBy(() -> filmService.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата релиза");
    }

    @Test
    void whenUpdateFilmIsSuccess() {
        Film film = Film.builder()
                .id(2)
                .name("name")
                .description("Description of film")
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(3)
                .name("nameUpd")
                .description("Description of filmUpd")
                .duration(2)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.updateFilm(filmUpd);
        FilmDto actual = filmService.getFilms().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(film.getName());
        Assertions.assertThat(actual.getDescription()).isEqualTo(film.getDescription());
        Assertions.assertThat(actual.getDuration()).isEqualTo(film.getDuration());
        Assertions.assertThat(actual.getReleaseDate()).isEqualTo(film.getReleaseDate());
    }

    @Test
    void whenUpdateFilmWithInvalidNameIsNotSuccess() {
        Film film = Film.builder()
                .name("name")
                .description("Description of film")
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("")
                .description("Description of filmUpd")
                .duration(2)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Кстати, реплицированные с зарубежных источников, современные исследования освещают чрезвычайно интересные особенности картины в целом, однако конкретные выводы, разумеется, функционально разнесены на независимые элементы! Внезапно, непосредственные участники технического прогресса будут представлены в исключительно положительном свете. Однозначно, непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть смешаны с не уникальными данными до степени совершенной неузнаваемости, из-за чего возрастает их статус бесполезности.")
                .duration(2)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Description of film")
                .duration(-11)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
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
                .duration(1)
                .releaseDate(LocalDate.now())
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        Film filmUpd = Film.builder()
                .id(1)
                .name("name")
                .description("Description of film")
                .duration(1)
                .releaseDate(LocalDate.ofYearDay(1000, 20))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        Assertions.assertThatThrownBy(() -> filmService.updateFilm(filmUpd))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не корректная дата релиза");
    }

    @Test
    void whenLikeFilmIsSuccess() {
        Film film = Film.builder()
                .id(1)
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film);
        User user = User.builder()
                .id(13)
                .name("Jerald Balistreri")
                .email("Litzy.Hettinger4@yahoo.com")
                .login("uONompBP6")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        filmService.like(2, 13);
        FilmDto actual = filmService.getFilms().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(film.getName());
        Assertions.assertThat(actual.getDescription()).isEqualTo(film.getDescription());
        Assertions.assertThat(actual.getDuration()).isEqualTo(film.getDuration());
        Assertions.assertThat(actual.getReleaseDate()).isEqualTo(film.getReleaseDate());
    }

    @Test
    void whenTopLikeFilmIsSuccess() {
        Film film = Film.builder()
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .build();
        filmService.addFilm(film);
        Film film1 = Film.builder()
                .id(1)
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film1);
        Film film2 = Film.builder()
                .id(1)
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film2);
        Film film3 = Film.builder()
                .id(1)
                .name("bjB4aIilbD419ye")
                .description("ZjjWhnWFXYvnI\n" +
                        "  │ RPxaxyJX9EJJlwEuQ5ettTbhSxwd7L26gF0SQ")
                .duration(104)
                .releaseDate(LocalDate.of(1968, 07, 31))
                .raiting(Raiting.builder().build())
                .genre(List.of(1))
                .build();
        filmService.addFilm(film3);
        User user = User.builder()
                .id(13)
                .name("Jerald Balistreri")
                .email("Litzy.Hettinger4@yahoo.com")
                .login("uONompBP6")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user);
        User user1 = User.builder()
                .id(14)
                .name("Jerald Balistreri")
                .email("Litzy.Hettinger4@yahoo.com")
                .login("uONompBP6")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user1);
        User user2 = User.builder()
                .id(15)
                .name("Jerald Balistreri")
                .email("Litzy.Hettinger4@yahoo.com")
                .login("uONompBP6")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user2);
        User user3 = User.builder()
                .id(16)
                .name("Jerald Balistreri")
                .email("Litzy.Hettinger4@yahoo.com")
                .login("uONompBP6")
                .birthday(LocalDate.ofYearDay(2000, 20))
                .build();
        userService.addUser(user3);
        filmService.like(6, 13);
        filmService.like(7, 14);
        filmService.like(7, 15);
        filmService.like(8, 16);
        filmService.mostPopularFilms(1000);
        FilmDto actual = filmService.getFilms().get(0);
        Assertions.assertThat(actual.getName()).isEqualTo(film.getName());
        Assertions.assertThat(actual.getDescription()).isEqualTo(film.getDescription());
        Assertions.assertThat(actual.getDuration()).isEqualTo(film.getDuration());
        Assertions.assertThat(actual.getReleaseDate()).isEqualTo(film.getReleaseDate());
    }
}