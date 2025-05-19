package com.swp391.hivtmss.model.payload.exception;

public class RegisterAccountExistedException extends RuntimeException {
    public RegisterAccountExistedException(String message) {
        super(message);
    }
}
