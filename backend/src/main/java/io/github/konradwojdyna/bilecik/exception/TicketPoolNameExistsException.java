package io.github.konradwojdyna.bilecik.exception;

public class TicketPoolNameExistsException extends RuntimeException {
    public TicketPoolNameExistsException(String name) {

        super("Ticket pool name already exists: " + name);
    }
}
