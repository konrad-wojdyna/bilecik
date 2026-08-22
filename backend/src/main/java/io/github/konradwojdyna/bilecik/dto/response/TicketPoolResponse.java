package io.github.konradwojdyna.bilecik.dto.response;

import io.github.konradwojdyna.bilecik.entity.Event;
import io.github.konradwojdyna.bilecik.entity.TicketPool;

import java.time.Instant;

public record TicketPoolResponse(
        Long id,
        String name,
        String note,
        Long priceGrosze,
        Integer quantity,
        Integer remaining,
        Long eventId
) {

    public static TicketPoolResponse from(TicketPool ticketPool) {
        return new TicketPoolResponse(
                ticketPool.getId(),
                ticketPool.getName(),
                ticketPool.getNote(),
                ticketPool.getPriceGrosze(),
                ticketPool.getQuantity(),
                ticketPool.getQuantity() - ticketPool.getSold(),
                ticketPool.getEvent().getId()
        );
    }

}
