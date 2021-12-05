package com.reloader.error;

import lombok.Data;

@Data
public class KeycloakException
{
    private String error;

    private String error_description;
}
