package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;


/**
 * User.
 */
@Data
public class User {
    private Integer id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
    private HashMap<Integer, Status> friendsId;


}
