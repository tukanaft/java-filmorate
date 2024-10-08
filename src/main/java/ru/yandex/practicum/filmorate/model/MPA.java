package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class MPA {
    Integer id;
    String raiting;
}
