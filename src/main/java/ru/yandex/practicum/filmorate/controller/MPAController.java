package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MPAController {
    @Autowired
    private final MPAService MPAService;

    @GetMapping
    public List<MPA> getMpas() {
        log.info("RaitingController: выполнение запроса на получение рейтингов");
        return MPAService.getMpas();
    }

    @GetMapping("/{id}")
    public MPA getMpaById(@PathVariable("id") Integer mpaId) {
        log.info("RaitingController: выполнение запроса на получение рейтинга");
        return MPAService.getMpaById(mpaId);
    }
}

