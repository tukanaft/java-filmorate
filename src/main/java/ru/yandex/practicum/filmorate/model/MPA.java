package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class MPA {
    private Integer id;
    private String name;
}
