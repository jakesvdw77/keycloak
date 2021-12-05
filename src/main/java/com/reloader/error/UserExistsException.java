package com.reloader.error;

import lombok.Data;

@Data
public class UserExistsException extends RuntimeException {

    private final String error;

    public UserExistsException(String error) {
        this.error = error;
    }
}
