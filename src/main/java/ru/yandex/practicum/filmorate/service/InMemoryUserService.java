package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserRequest;
import ru.yandex.practicum.filmorate.exception.BadInputExceptionParametered;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final EventService eventService;

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        checkUserId(userRepository, userId);
        checkUserId(userRepository, friendId);
        if (userRepository.isFriendRequest(userId, friendId)) {
            return userRepository.acceptRequest(userId, friendId);
        }
        eventService.addEvent(new Event(userId, EventType.FRIEND, OperationType.ADD, friendId, Instant.now().toEpochMilli()));
        return userRepository.addFriend(userId, friendId);
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        checkUserId(userRepository, userId);
        checkUserId(userRepository, friendId);
        if (userRepository.isFriend(userId, friendId)) {
            return userRepository.removeRequest(userId, friendId);
        }
        eventService.addEvent(new Event(userId, EventType.FRIEND, OperationType.REMOVE, friendId, Instant.now().toEpochMilli()));
        return userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public Set<UserDto> getCommonFriends(Long userId, Long otherId) {
        checkUserId(userRepository, userId);
        checkUserId(userRepository, otherId);
        return userRepository.getCommonFriends(userId, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserDto> getFriends(Long userId) {
        checkUserId(userRepository, userId);
        return userRepository.getFriends(userId).stream()
                .map(UserMapper::mapToUserDto)
                .sorted(Comparator.comparingLong(UserDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getName().isBlank()) {
            userRequest.setName(userRequest.getLogin());
        }
        User user = UserMapper.mapToUser(userRequest);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        checkUserId(userRepository, userId);
        userRepository.delete(userId);
    }

    @Override
    public UserDto get(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + id + " не найден"));
    }


    @Override
    public UserDto update(UserRequest userRequest) {
        if (userRequest.getId() == null) {
            throw new BadInputExceptionParametered("ID", "Должен быть указан ID");
        }
        User updatedUser = userRepository.findById(userRequest.getId())
                .map(user -> UserMapper.updateUserFields(user, userRequest))
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userRequest.getId() + " не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public Collection<FilmDto> getRecommendations(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("Пользователь с ID {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Collection<Film> filmsLikedByUser = filmRepository.getLikedFilmsByUserId(userId);

        User mostSimilarUser = null;
        int maxSimilarity = 0;

        Set<User> usersWithSameLikes = userRepository.getUsersLikedSameFilms(userId);

        for (User otherUser : usersWithSameLikes) {
            List<Film> filmsLikedByOtherUser = new ArrayList<>(filmRepository.getLikedFilmsByUserId(otherUser.getId()));

            filmsLikedByOtherUser.removeAll(filmsLikedByUser);

            int similarity = filmsLikedByOtherUser.size();
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                mostSimilarUser = otherUser;
            }
        }

        if (mostSimilarUser != null && maxSimilarity > 0) {
            List<Film> recommendedFilms = new ArrayList<>(filmRepository.getLikedFilmsByUserId(mostSimilarUser.getId()));
            recommendedFilms.removeAll(filmsLikedByUser);

            return recommendedFilms.stream()
                    .map(FilmMapper::mapToFilmDto)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void checkUserId(UserRepository userRepository, Long... ids) {
        for (Long id : ids) {
            if (userRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Юзера с ID " + id + " не существует");
            }
        }
    }
}
