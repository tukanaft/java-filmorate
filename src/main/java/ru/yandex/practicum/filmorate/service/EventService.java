package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Service
public interface EventService {
    void addEvent(Event event);

    List<Event> getUserFeed(Long userId);
}
