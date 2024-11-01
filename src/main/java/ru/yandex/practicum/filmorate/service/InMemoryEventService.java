package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.EventRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryEventService implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public void addEvent(Event event) {
        eventRepository.saveEvent(event);
    }

    @Override
    public List<Event> getUserFeed(Long userId) {
        if (!eventRepository.existsByUserId(userId)) {
            throw new NotFoundException("Нет событий для данного пользователя");
        }
        checkUserId(userRepository, userId);
        return eventRepository.getUserFeed(userId);
    }

    public void checkUserId(UserRepository userRepository, Long... ids) {
        for (Long id : ids) {
            if (userRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Юзера с ID " + id + " не существует");
            }
        }
    }
}
