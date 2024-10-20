package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    @Autowired
    private final MpaService mpaService;

    @GetMapping
    public List<MPA> getMpas() {
        log.info("MpaController: выполнение запроса на получение рейтингов");
        return mpaService.getMpas();
    }

    @GetMapping("/{id}")
    public MPA getMpaById(@PathVariable("id") Integer mpaId) {
        log.info("MpaController: выполнение запроса на получение рейтинга");
        return mpaService.getMpaById(mpaId);
    }
}

