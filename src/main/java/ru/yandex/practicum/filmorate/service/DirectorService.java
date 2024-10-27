package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.DirectorRequest;

import java.util.List;

@Service
public interface DirectorService {
    List<DirectorDto> getAll();

    DirectorDto get(Long id);

    boolean delete(Long id);

    DirectorDto save(DirectorRequest request);

    DirectorDto update(DirectorRequest request);
}
