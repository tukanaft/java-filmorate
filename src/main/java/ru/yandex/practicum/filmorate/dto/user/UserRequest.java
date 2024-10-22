package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UserRequest {
    private Long id;
    @NotBlank(message = "E-mail не может быть пустым.")
    @Email(message = "Некорректный формат E-mail")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$")
    private String login;
    private String name;
    @Past(message = "Некорректная дата рождения.")
    private LocalDate birthday;
    private Set<Long> friendsSet;
}
