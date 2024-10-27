package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;

import java.util.List;

@Service
public interface RatingService {
    RatingDto get(Long id);

    List<RatingDto> getAll();
}
