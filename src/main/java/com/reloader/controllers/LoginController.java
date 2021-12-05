package com.reloader.controllers;

import com.reloader.models.LoginUser;
import com.reloader.models.NewUser;
import com.reloader.models.ResponseDTO;
import com.reloader.models.UserProfileDTO;
import com.reloader.services.KeyCloakService;
import com.reloader.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private KeyCloakService keyCloakService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<UserProfileDTO>> login(@RequestBody LoginUser user) {
        var result = keyCloakService.authenticate(user);

        var response = new ResponseDTO<>(null, result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Boolean>> registerUser(@RequestBody NewUser newUser)
    {
        keyCloakService.createUser(newUser);

        var response = new ResponseDTO<>(null, true);

        return ResponseEntity.ok(response);
    }

}
