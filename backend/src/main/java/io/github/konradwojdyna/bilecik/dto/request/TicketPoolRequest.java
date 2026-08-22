package io.github.konradwojdyna.bilecik.dto.request;

import io.github.konradwojdyna.bilecik.entity.TicketPool;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TicketPoolRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Size(max = 100, message = "Note length max description is 100")
        String note,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        Long priceGrosze,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        Integer quantity
) {

        public void applyTo(TicketPool ticketPool) {
                ticketPool.setName(name);
                ticketPool.setNote(note);
                ticketPool.setPriceGrosze(priceGrosze);
                ticketPool.setQuantity(quantity);
        }

        public TicketPool toEntity() {
                TicketPool ticketPool = new TicketPool();
                applyTo(ticketPool);
                return ticketPool;
        }
}
