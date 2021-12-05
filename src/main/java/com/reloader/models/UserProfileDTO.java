package com.reloader.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String token;

    private Long expires;

}
