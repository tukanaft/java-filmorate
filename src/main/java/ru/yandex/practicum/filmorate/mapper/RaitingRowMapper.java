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
        MPA mpa = new MPA();
        mpa.setId(resultSet.getInt("id"));
        mpa.setName(resultSet.getString("name"));

        return mpa;
    }
}
