package ru.yandex.practicum.filmorate.model;

import lombok.*;


import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
