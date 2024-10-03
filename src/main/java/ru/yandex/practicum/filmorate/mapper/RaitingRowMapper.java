package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Raiting;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RaitingRowMapper implements RowMapper<Raiting> {


    @Override
    public Raiting mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Raiting raiting = new Raiting();
        raiting.setId(resultSet.getInt("id"));
        raiting.setRaiting(resultSet.getString("raiting"));

        return raiting;
    }
}
