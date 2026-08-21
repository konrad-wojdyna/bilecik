package io.github.konradwojdyna.bilecik.controller;


import io.github.konradwojdyna.bilecik.dto.request.TicketPoolRequest;
import io.github.konradwojdyna.bilecik.dto.response.TicketPoolResponse;
import io.github.konradwojdyna.bilecik.service.TicketPoolService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-pools")
public class TicketPoolController {

    private final TicketPoolService ticketPoolService;

    public TicketPoolController(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }

    @GetMapping("/{ticketPoolId}")
    public ResponseEntity<TicketPoolResponse> getTicketPoolById(
            @PathVariable Long eventId,
            @PathVariable Long  ticketPoolId
    ){
        TicketPoolResponse ticketPoolResponse = ticketPoolService.getTicketPoolById(eventId, ticketPoolId);
        return ResponseEntity.ok(ticketPoolResponse);
    }

    @GetMapping
    public ResponseEntity<List<TicketPoolResponse>> getTicketPools(@PathVariable Long eventId) {
        List<TicketPoolResponse> ticketPoolResponseList = ticketPoolService.getTicketPools(eventId);
        return ResponseEntity.ok(ticketPoolResponseList);
    }

    @PostMapping
    public ResponseEntity<TicketPoolResponse> addTicketPool(
            @PathVariable Long eventId,
            @Valid @RequestBody TicketPoolRequest ticketPoolRequest
    ){
        TicketPoolResponse ticketPoolResponse = ticketPoolService.addTicketPool(eventId, ticketPoolRequest);
        URI location = URI.create("/api/v1/events/" + eventId + "/ticket-pools/" + ticketPoolResponse.id());

        return ResponseEntity.created(location).body(ticketPoolResponse);
    }

    @PutMapping("/{ticketPoolId}")
    public ResponseEntity<TicketPoolResponse> updateTicketPool(
            @PathVariable Long eventId,
            @PathVariable Long ticketPoolId,
            @Valid @RequestBody TicketPoolRequest ticketPoolRequest
    ){
        TicketPoolResponse ticketPoolResponse = ticketPoolService
                .updateTicketPool(eventId, ticketPoolId, ticketPoolRequest);
        return ResponseEntity.ok(ticketPoolResponse);
    }

    @DeleteMapping("/{ticketPoolId}")
    public ResponseEntity<Void> deleteTicketPool(
            @PathVariable Long eventId,
            @PathVariable Long ticketPoolId
    ){

        ticketPoolService.deleteTicketPool(eventId, ticketPoolId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ticketPoolId}/purchase")
    public ResponseEntity<TicketPoolResponse> purchaseTicket(
            @PathVariable Long eventId,
            @PathVariable Long ticketPoolId
    ){

        TicketPoolResponse response = ticketPoolService.purchaseTicket(eventId, ticketPoolId);
        return ResponseEntity.ok(response);
    }
}
