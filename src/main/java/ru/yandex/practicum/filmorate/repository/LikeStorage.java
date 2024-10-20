package ru.yandex.practicum.filmorate.repository;

public interface LikeStorage {
    Boolean isAlreadyLiked(Integer filmId, Integer userId);
}
