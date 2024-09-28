package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "Friends")
@Getter
@Setter
@NoArgsConstructor
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;
    Integer userid;
    Integer friend;
    Status status;
}
