package ru.yandex.practicum.filmorate.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Event {
    private Long eventId;
    private Long timestamp;
    private Long userId;
    private EventType eventType;
    private OperationType operation;
    private Long entityId;

    public Event(Long userId, EventType eventType, OperationType operation, Long entityId, Long timestamp) {
        this.timestamp = timestamp != null ? timestamp : Instant.now().toEpochMilli();
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}