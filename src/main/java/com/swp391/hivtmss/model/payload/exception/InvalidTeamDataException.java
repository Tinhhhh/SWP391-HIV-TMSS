package com.swp391.hivtmss.model.payload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTeamDataException extends RuntimeException {
    public InvalidTeamDataException(String message) {
        super(message);
    }
}
