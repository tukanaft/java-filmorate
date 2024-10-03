package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.RaitingRowMapper;
import ru.yandex.practicum.filmorate.model.Raiting;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaitingService {
    JdbcTemplate jdbcTemplate;
    RaitingRowMapper raitingRowMapper;

    public List<Raiting> getRaitings() {
        String query = "Select * from raiting";
        return jdbcTemplate.query(query, raitingRowMapper);
    }

    public Raiting getRaitingById(Integer raitingId) {
        String query = "Select * from raiting where id = ?";
        return jdbcTemplate.queryForObject(query, raitingRowMapper, raitingId);
    }

}
