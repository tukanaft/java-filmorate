package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserRequest;
import ru.yandex.practicum.filmorate.exception.BadInputExceptionParametered;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserRepository userRepository;

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

    public void checkUserId(UserRepository userRepository, Long... ids) {
        for (Long id : ids) {
            if (userRepository.findById(id).isEmpty()) {
                throw new NotFoundException("Юзера с ID " + id + " не существует");
            }
        }
    }
}
