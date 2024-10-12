package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RaitingRowMapper implements RowMapper<MPA> {


    @Override
    public MPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MPA MPA = new MPA();
        MPA.setId(resultSet.getInt("id"));
        MPA.setName(resultSet.getString("name"));

        return MPA;
    }
}
