package com.reloader.models;

import lombok.Data;

@Data
public class NewUser {

    private String email;

    private String name;

    private String surname;

    private Boolean consent;

    private String password;
}
