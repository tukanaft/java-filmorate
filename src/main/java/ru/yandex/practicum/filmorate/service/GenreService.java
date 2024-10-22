package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;

import java.util.List;

@Service
public interface GenreService {
    GenreDto get(Long id);

    List<GenreDto> getAll();
}
