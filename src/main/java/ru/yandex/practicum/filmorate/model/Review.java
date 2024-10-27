package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Review {
    private Integer reviewId;
    private String content;
    @JsonProperty
    private boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private Integer useful;
    //Рейтинг - калькулируемая величина в зависимости от кол-ва выставленных пользователями полезностей этого отзыва.
    private Integer reviewRate;
}
