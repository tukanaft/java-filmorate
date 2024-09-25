package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;


/**
 * User.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User {
    private Integer id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
    private ArrayList<Integer> friendsId;


}
