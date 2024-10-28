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
    private Long reviewId;
    private String content;
    @JsonProperty
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}
