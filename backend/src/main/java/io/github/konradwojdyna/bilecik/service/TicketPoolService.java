package io.github.konradwojdyna.bilecik.service;

import io.github.konradwojdyna.bilecik.dto.request.TicketPoolRequest;
import io.github.konradwojdyna.bilecik.dto.response.TicketPoolResponse;
import io.github.konradwojdyna.bilecik.entity.Event;
import io.github.konradwojdyna.bilecik.entity.TicketPool;
import io.github.konradwojdyna.bilecik.exception.EventNotFoundException;
import io.github.konradwojdyna.bilecik.exception.TicketPoolNameExistsException;
import io.github.konradwojdyna.bilecik.exception.TicketPoolNotFoundException;
import io.github.konradwojdyna.bilecik.repository.EventRepository;
import io.github.konradwojdyna.bilecik.repository.TicketPoolRepository;
import io.github.konradwojdyna.bilecik.exception.TicketPoolSoldOutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketPoolService {

    private final TicketPoolRepository ticketPoolRepository;
    private final EventRepository eventRepository;

    public TicketPoolService(TicketPoolRepository ticketPoolRepository, EventRepository eventRepository) {
        this.ticketPoolRepository = ticketPoolRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public TicketPoolResponse getTicketPoolById(
            Long eventId,
            Long ticketPoolId
    ){
       return TicketPoolResponse.from(findPool(eventId, ticketPoolId));
    }

    @Transactional(readOnly = true)
    public List<TicketPoolResponse> getTicketPools(Long eventId){

        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<TicketPool> ticketPools = ticketPoolRepository.findByEventId(eventId);

        return  ticketPools.stream().map(TicketPoolResponse::from).toList();
    }

    @Transactional
    public TicketPoolResponse addTicketPool(Long eventId, TicketPoolRequest ticketPoolRequest){

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));

        if(ticketPoolRepository.existsByEventIdAndName(eventId, ticketPoolRequest.name())){
            throw new TicketPoolNameExistsException(ticketPoolRequest.name());
        }

        TicketPool ticketPoolToEntity = ticketPoolRequest.toEntity();
        event.addTicketPool(ticketPoolToEntity);

        TicketPool ticketPool = ticketPoolRepository.save(ticketPoolToEntity);

        return TicketPoolResponse.from(ticketPool);
    }

    @Transactional
    public TicketPoolResponse updateTicketPool(Long eventId, Long ticketId,
                                               TicketPoolRequest ticketPoolRequest){

        TicketPool ticketPool = findPool(eventId, ticketId);

        if(ticketPoolRepository.existsByEventIdAndNameAndIdNot(eventId, ticketPoolRequest.name(),
        ticketId)){
            throw new TicketPoolNameExistsException(ticketPoolRequest.name());
        }

        ticketPoolRequest.applyTo(ticketPool);

        return TicketPoolResponse.from(ticketPool);
    }

    @Transactional
    public void deleteTicketPool(Long eventId, Long ticketPoolId){
        ticketPoolRepository.delete(findPool(eventId, ticketPoolId));
    }

    private TicketPool findPool(Long eventId, Long ticketPoolId){
        return ticketPoolRepository.findByIdAndEventId(ticketPoolId, eventId)
                .orElseThrow(() -> new TicketPoolNotFoundException(ticketPoolId));
    }


    @Transactional
    public TicketPoolResponse purchaseTicket(
            Long eventId,
            Long ticketPoolId
    ){

        TicketPool ticketPool = findPool(eventId, ticketPoolId);

        if(ticketPool.getSold() >= ticketPool.getQuantity()){
            throw new TicketPoolSoldOutException(ticketPoolId);
        }

        ticketPool.setSold(ticketPool.getSold() + 1);

        return TicketPoolResponse.from(ticketPool);
    }
}
