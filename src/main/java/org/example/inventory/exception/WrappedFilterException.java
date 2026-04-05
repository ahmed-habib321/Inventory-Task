package org.example.inventory.exception;

import jakarta.servlet.ServletException;

import java.io.IOException;

public class WrappedFilterException extends RuntimeException {
    private final Exception cause;

    public WrappedFilterException(Exception cause) {
        super(cause);
        this.cause = cause;
    }

    public void rethrow() throws IOException, ServletException {
        if (cause instanceof IOException e) throw e;
        if (cause instanceof ServletException e) throw e;
        throw new ServletException(cause);
    }
}
