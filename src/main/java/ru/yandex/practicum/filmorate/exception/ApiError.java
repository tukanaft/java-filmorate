package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    @JsonFormat (shape = JsonFormat.Shape.STRING,pattern = "yyyy.MM.dd hh:mm:ss")
    private LocalDateTime errorTime;

}
