package io.github.konradwojdyna.bilecik.service;

import io.github.konradwojdyna.bilecik.dto.request.EventRequest;
import io.github.konradwojdyna.bilecik.dto.response.EventResponse;
import io.github.konradwojdyna.bilecik.entity.Event;
import io.github.konradwojdyna.bilecik.exception.EventNotFoundException;
import io.github.konradwojdyna.bilecik.repository.EventRepository;
import io.github.konradwojdyna.bilecik.repository.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
   public Page<EventResponse> getEvents(
           String city,
           String category,
           Pageable pageable
    ){

        Specification<Event> spec = Specification.allOf(
                EventSpecification.hasCity(city),
                EventSpecification.hasCategory(category)
        );

        return eventRepository.findAll(spec, pageable).map(EventResponse::from);
   }

   @Transactional(readOnly = true)
   public EventResponse getEventById(Long id){

        Event event = eventRepository.findById(id).orElseThrow(() ->
                new EventNotFoundException(id));

        return EventResponse.from(event);
   }

   @Transactional
   public EventResponse addEvent(EventRequest createEventRequest){

        Event eventToEntity = createEventRequest.toEntity();

        Event newEvent = eventRepository.save(eventToEntity);

       return EventResponse.from(newEvent);
   }

   @Transactional
   public EventResponse updateEvent(Long id, EventRequest updateEventRequest){

    Event event = eventRepository.findById(id).orElseThrow(
             () -> new EventNotFoundException(id));

    updateEventRequest.applyTo(event);

     return EventResponse.from(event);
   }


   @Transactional
   public void deleteEvent(Long id){

       Event event = eventRepository.findById(id).orElseThrow(() ->
               new EventNotFoundException(id));

       eventRepository.delete(event);
   }
}
