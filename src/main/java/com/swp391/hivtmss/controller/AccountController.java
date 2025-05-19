package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account", description = "Method for account settings required access token to gain access")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Change account's password", description = "Change account's password")
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestParam String oldPassword, @RequestParam String newPassword) {
        accountService.changePassword(request, oldPassword, newPassword);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Change password successfully");
    }

    @Operation(summary = "Get current account info", description = "Get current account info")
    @GetMapping
    public ResponseEntity<Object> getCurrentAccountInfo(HttpServletRequest request) {
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Get current account info successfully", accountService.getCurrentAccountInfo(request));
    }

}
