package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Data
public class Friends {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private String status;
}
