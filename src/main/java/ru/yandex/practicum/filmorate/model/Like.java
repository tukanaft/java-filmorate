package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Data
public class Like {
    private Integer id;
    private Integer userId;
    private Integer filmId;
}
