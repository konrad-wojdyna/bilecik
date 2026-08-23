package io.github.konradwojdyna.bilecik.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {

        super("Email already exists: " + email);
    }
}
