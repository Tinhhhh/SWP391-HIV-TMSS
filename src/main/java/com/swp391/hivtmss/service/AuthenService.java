package com.swp391.hivtmss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swp391.hivtmss.model.payload.request.AuthenticationRequest;
import com.swp391.hivtmss.model.payload.request.RegistrationRequest;
import com.swp391.hivtmss.model.payload.response.AuthenticationResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.NoSuchAlgorithmException;

public interface AuthenService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    void forgotPassword(String email) throws NoSuchAlgorithmException, MessagingException;

    void resetPassword(String email, String password, String token) throws JsonProcessingException;


}
