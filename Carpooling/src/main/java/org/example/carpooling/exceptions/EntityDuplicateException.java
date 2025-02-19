package org.example.carpooling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists", type, attribute, value));
    }
}
