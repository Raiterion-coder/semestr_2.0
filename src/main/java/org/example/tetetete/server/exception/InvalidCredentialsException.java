package org.example.tetetete.server.exception;

/**
 * Исключение, возникающее при неверных учетных данных при входе.
 */

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}