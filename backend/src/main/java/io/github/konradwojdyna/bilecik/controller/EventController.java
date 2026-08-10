package io.github.konradwojdyna.bilecik.controller;


import io.github.konradwojdyna.bilecik.dto.request.EventRequest;
import io.github.konradwojdyna.bilecik.dto.response.EventResponse;
import io.github.konradwojdyna.bilecik.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(
             @RequestParam(required = false) String city,
             @RequestParam(required = false) String category,
             Pageable pageable
    ) {
        Page<EventResponse> events = eventService.getEvents(city, category, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        EventResponse event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventResponse> addEvent(@Valid @RequestBody EventRequest eventRequest) {
        EventResponse event = eventService.addEvent(eventRequest);
        URI location = URI.create("/api/v1/events/" + event.id());
        return ResponseEntity.created(location).body(event);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,@Valid @RequestBody EventRequest eventRequest) {
        EventResponse event = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
