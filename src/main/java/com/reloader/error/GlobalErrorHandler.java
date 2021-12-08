package com.reloader.error;


import com.reloader.models.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<Object> handleUserNotFound(UserNotFoundException exception) {
        log.error(exception.getError());

        Set<String> errors = new HashSet<>();
        errors.add(exception.getError());

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setErrors(errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(responseDTO);

    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<Object> handleUserExist(UserExistsException exception) {
        log.error(exception.getError());

        Set<String> errors = new HashSet<>();
        errors.add(exception.getError());

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(responseDTO);

    }


    @ExceptionHandler(KeycloakError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<Object> handleKeycloakError(KeycloakError exception) {
        log.error(exception.getError());

        Set<String> errors = new HashSet<>();
        errors.add(exception.getError());

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setErrors(errors);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(responseDTO);

    }



    @ExceptionHandler(AuthorizationError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<Object> handleUserExist(AuthorizationError exception) {
        log.error(exception.getError());

        Set<String> errors = new HashSet<>();
        errors.add(exception.getError());

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setErrors(errors);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(responseDTO);

    }

}
