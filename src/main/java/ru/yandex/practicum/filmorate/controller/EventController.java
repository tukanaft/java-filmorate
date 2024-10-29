package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/feed")
public class EventController {
    EventService eventService;

    @GetMapping
    public List<Event> getUserFeed(@PathVariable("id") Long userId) {
        return eventService.getUserFeed(userId);
    }

}
