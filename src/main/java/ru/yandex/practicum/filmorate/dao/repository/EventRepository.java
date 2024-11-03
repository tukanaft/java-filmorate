package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.Timestamp;
import java.util.List;

@Repository("eventRepository")
public class EventRepository extends BaseRepository<Event> {
    private static final String INSERT_QUERY = "INSERT INTO users_feed (user_id, operation, event_type, timestamp, entity_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_USER_FEED = "SELECT event_id, user_id, operation, event_type, timestamp, entity_id FROM users_feed WHERE user_id = ? ";
    private static final String FIND_BY_USER_ID = "SELECT COUNT(*) FROM users_feed WHERE user_id = ?";

    public EventRepository(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    public void saveEvent(Event event) {
        Long timestampMillis = event.getTimestamp();
        Timestamp timestamp = new Timestamp(timestampMillis);
        long id = insert(INSERT_QUERY, event.getUserId(), event.getOperation().toString(),
                event.getEventType().toString(), timestamp, event.getEntityId());
        event.setEventId(id);
    }

    public boolean existsByUserId(Long userId) {
        Long count = jdbc.queryForObject(FIND_BY_USER_ID, Long.class, userId);
        return count != null && count > 0;
    }

    public List<Event> getUserFeed(Long userId) {
        return findMany(GET_USER_FEED, userId);
    }
}
