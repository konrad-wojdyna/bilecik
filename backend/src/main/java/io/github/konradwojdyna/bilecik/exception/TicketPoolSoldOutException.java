package io.github.konradwojdyna.bilecik.exception;

public class TicketPoolSoldOutException extends RuntimeException {

    public TicketPoolSoldOutException(Long ticketPoolId) {
        super("Ticket pool with id " + ticketPoolId + " is sold out");
    }

}
