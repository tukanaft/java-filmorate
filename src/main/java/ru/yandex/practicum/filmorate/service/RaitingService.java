package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RaitingRowMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaitingService {
    private final JdbcTemplate jdbcTemplate;
    private final RaitingRowMapper raitingRowMapper;
    private final FilmDbStorage filmDbStorage;

    public List<MPA> getRaitings() {
        String query = "Select * from ratings";
        return jdbcTemplate.query(query, raitingRowMapper);
    }

    public MPA getRaitingById(Integer raitingId) {
        if (!filmDbStorage.ifMPAExists(raitingId)){
           throw new NotFoundException("рейтинга не существует", raitingId);
        }
        String query = "Select * from ratings where id = ?";
        return jdbcTemplate.queryForObject(query, raitingRowMapper, raitingId);
    }

}
