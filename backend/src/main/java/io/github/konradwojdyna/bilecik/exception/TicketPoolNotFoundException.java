package io.github.konradwojdyna.bilecik.exception;

public class TicketPoolNotFoundException extends RuntimeException {
    public TicketPoolNotFoundException(Long ticketPoolId) {
        super("Ticket pool with id " + ticketPoolId + " not found");
    }
}
