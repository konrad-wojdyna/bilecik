package io.github.konradwojdyna.bilecik.dto.request;

import io.github.konradwojdyna.bilecik.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record EventRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotBlank(message = "Venue is required")
        String venue,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City name is max 100 length")
        String city,

        @Size(max = 100, message = "Category name is max 100 length")
        String category,

        @NotNull(message = "Starts at is required")
        Instant startsAt,

        @NotBlank(message = "Timezone is required")
        String timezone,

        @NotNull(message = "Capacity is required")
        Integer capacity,

        Integer durationMinutes,

        Integer minAge
) {

        public void applyTo(Event event){
                event.setTitle(title);
                event.setDescription(description);
                event.setVenue(venue);
                event.setCity(city);
                event.setCategory(category);
                event.setStartsAt(startsAt);
                event.setTimezone(timezone);
                event.setCapacity(capacity);
                event.setDurationMinutes(durationMinutes);
                event.setMinAge(minAge);
        }

        public Event toEntity(){
                Event event = new Event();
                applyTo(event);
                return  event;
        }

}
