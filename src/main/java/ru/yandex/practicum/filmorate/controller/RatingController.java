package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.service.RatingService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {
    RatingService ratingService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto get(@PathVariable Long id) {
        return ratingService.get(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RatingDto> getAll() {
        return ratingService.getAll();
    }
}
