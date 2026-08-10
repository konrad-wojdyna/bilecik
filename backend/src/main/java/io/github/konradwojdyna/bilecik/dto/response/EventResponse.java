package io.github.konradwojdyna.bilecik.dto.response;

import io.github.konradwojdyna.bilecik.entity.Event;

import java.time.Instant;

public record EventResponse(
    Long id,
    String title,
    String description,
    String venue,
    String city,
    String category,
    Instant startsAt,
    String timezone,
    Integer capacity,
    Integer durationMinutes,
    Integer minAge,
    Instant createdAt,
    Instant updatedAt
) {

    public static EventResponse from(Event event){
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue(),
                event.getCity(),
                event.getCategory(),
                event.getStartsAt(),
                event.getTimezone(),
                event.getCapacity(),
                event.getDurationMinutes(),
                event.getMinAge(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
