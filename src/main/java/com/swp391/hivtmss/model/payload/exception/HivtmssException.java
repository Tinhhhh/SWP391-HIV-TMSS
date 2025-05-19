package com.swp391.hivtmss.model.payload.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HivtmssException extends RuntimeException {
    private HttpStatus httpStatus;

    public HivtmssException(String message) {
        super(message);
    }

    public HivtmssException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
