package com.reloader.error;

import lombok.Data;

@Data
public class AuthorizationError extends RuntimeException {
    private final String error;

    public AuthorizationError(String error) {
        this.error = error;
    }
}
