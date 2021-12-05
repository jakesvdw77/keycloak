package com.reloader.error;

import com.google.gson.Gson;

public class KeycloakError extends RuntimeException {
    private static final Gson gson = new Gson();

    private KeycloakException exception;

    public KeycloakError(String error) {
        if (error != null && error.contains("{")) {
            error = error.substring(error.indexOf("{"));
            error = error.substring(0, error.indexOf("}") + 1);
            exception = gson.fromJson(error, KeycloakException.class);
        }
    }

    public String getError() {
        if (exception != null)
            return exception.getError_description();

        return "Unknown Error";
    }
}
