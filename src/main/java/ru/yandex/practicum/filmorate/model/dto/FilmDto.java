package ru.yandex.practicum.filmorate.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Film.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class FilmDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private List<Integer> likes;
    private List<String> genre;
    private String raiting;
}
