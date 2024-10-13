package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Genre {
    private Integer id;
    private String name;
}
