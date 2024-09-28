package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dataBase.RaitingDb;
import ru.yandex.practicum.filmorate.model.Raiting;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaitingService {
    @Autowired
    RaitingDb raitingDb;

    public List<Raiting> getRaitings() {
        return raitingDb.findAll();
    }

    public Raiting getRaitingById(Integer raitingId) {
        return raitingDb.findById(raitingId).get();
    }

}
