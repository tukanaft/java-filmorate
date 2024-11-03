package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("eventRowMapper")
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        EventType eventType = EventType.valueOf(rs.getString("event_type"));
        OperationType operation = OperationType.valueOf(rs.getString("operation"));
        Long timestamp = rs.getTimestamp("timestamp").getTime();
        Long entityId = rs.getLong("entity_id");
        Long eventId = rs.getLong("event_id");

        Event event = new Event(userId, eventType, operation, entityId, timestamp);
        event.setEventId(eventId);
        return event;
    }
}
