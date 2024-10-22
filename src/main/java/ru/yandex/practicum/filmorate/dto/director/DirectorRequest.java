package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorRequest {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым.")
    private String name;
}
