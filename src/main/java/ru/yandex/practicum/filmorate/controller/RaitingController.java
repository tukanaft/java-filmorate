package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Raiting;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RaitingService;

import java.util.List;


    @Slf4j
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/mpa")
    public class RaitingController {
        @Autowired
        private final RaitingService raitingService;

        @GetMapping
        public List<Raiting> getRaiting() {
            log.info("RaitingController: выполнение запроса на получение рейтингов");
            return raitingService.getRaitings();
        }

        @GetMapping("/{id}")
        public Raiting getRaiting(@PathVariable("id")Integer raitingId){
            log.info("RaitingController: выполнение запроса на получение рейтинга");
            return raitingService.getRaitingById(raitingId);
        }
    }

