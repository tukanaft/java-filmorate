package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "Likes")
@Getter
@Setter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;
    Integer userId;
    Integer filmId;
}
