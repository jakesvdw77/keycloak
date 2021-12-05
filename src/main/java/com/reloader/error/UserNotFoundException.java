package com.reloader.error;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException {
    private final String error;

    public UserNotFoundException(String error) {
        this.error = error;
    }
}
