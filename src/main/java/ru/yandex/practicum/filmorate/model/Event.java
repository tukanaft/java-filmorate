package ru.yandex.practicum.filmorate.model;



import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private Long eventId;
    private Long timestamp;
    @JsonProperty
    private Long userId;
    private EventType eventType;
    private OperationType operation;
    @JsonProperty
    private Long entityId;

    public Event(Long userId, EventType eventType, OperationType operation, Long entityId, Long timestamp) {
        this.timestamp = timestamp != null ? timestamp : Instant.now().toEpochMilli();
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}