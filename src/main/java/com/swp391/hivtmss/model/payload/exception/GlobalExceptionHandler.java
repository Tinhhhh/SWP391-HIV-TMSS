package com.swp391.hivtmss.model.payload.exception;

import com.swp391.hivtmss.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(500)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(500)
                                .timestamp(DateUtil.formatTimestamp(DateUtil.convertUtcToIctDate(Instant.now()), DateUtil.DATE_TIME_FORMAT))
                                .message("Internal Server Error. Please contact administrator for more information.")
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(HivtmssException.class)
    public ResponseEntity<ExceptionResponse> handleHivtmssException(HivtmssException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus().value())
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(exception.getHttpStatus().value())
                                .timestamp(DateUtil.formatTimestamp(DateUtil.convertUtcToIctDate(Instant.now()), DateUtil.DATE_TIME_FORMAT))
                                .message(exception.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                                .timestamp(DateUtil.formatTimestamp(new Date(), DateUtil.DATE_TIME_FORMAT))
                                .message("Authentication failed. Please check your credentials.")
                                .error(exception.getMessage())
                                .build()
                );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(HttpStatus.BAD_REQUEST.value())
                                .timestamp(DateUtil.formatTimestamp(DateUtil.convertUtcToIctDate(Instant.now()), DateUtil.DATE_TIME_FORMAT))
                                .data(errors)
                                .build()
                );

    }

    @ExceptionHandler(RegisterAccountExistedException.class)
    public ResponseEntity<ExceptionResponse> handleRegistrationAccountExistedResponse(RegisterAccountExistedException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(HttpStatus.BAD_REQUEST.value())
                                .timestamp(DateUtil.formatTimestamp(DateUtil.convertUtcToIctDate(Instant.now()), DateUtil.DATE_TIME_FORMAT))
                                .message("Registration request failed. Account already existed.")
                                .error(exception.getMessage())
                                .build()
                );
    }

}
