package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.enums.SortByRole;
import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.request.NewAccount;
import com.swp391.hivtmss.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.swp391.hivtmss.util.AppConstants.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account", description = "Method for account settings required access token to gain access")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Change account's password", description = "Change account's password")
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(HttpServletRequest request,@RequestParam String oldPassword, @RequestParam String newPassword) {
        accountService.changePassword(request, oldPassword, newPassword);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Change password successfully");
    }

    @Operation(summary = "Get current account info", description = "Get current account info")
    @GetMapping
    public ResponseEntity<Object> getCurrentAccountInfo(HttpServletRequest request) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Get current account info successfully", accountService.getCurrentAccountInfo(request));
    }

    @Operation(summary = "Update current account info by user", description = "Update current account info by user")
    @PutMapping
    public ResponseEntity<Object> updateAccount(HttpServletRequest request,@Valid @RequestBody EditAccount account) {
        accountService.updateAccountInfo(request, account);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Update account successfully");
    }


    @Operation(summary = "Get account info by id for admin", description = "Get account info by id for admin")
    @GetMapping("/admin")
    public ResponseEntity<Object> getAccountForAdmin(@RequestParam("id") UUID id) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Get account info by id successfully",
                accountService.getAccountForAdmin(id));
    }

    @Operation(summary = "Get all accounts info for admin", description = "get all accounts info for admin")
    @GetMapping("/admin/all")
    public ResponseEntity<Object> getAccountsForAdmin(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "role") SortByRole role
    ) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Get accounts for admin successfully",
                accountService.getAccountsForAdmin(pageNo, pageSize, keyword, sortBy, sortDir, role));
    }

    @Operation(summary = "Edit account by admin", description = "Edit account by admin")
    @PutMapping("/admin")
    public ResponseEntity<Object> editAccount(@RequestParam("id") UUID id, @RequestBody EditAccountByAdmin account) {
        accountService.editAccountByAdmin(id, account);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Edit account successfully");
    }

    @Operation(summary = "Create new account by admin for doctor, Manager", description = "Create new account by admin")
    @PostMapping("/admin/new-account")
    public ResponseEntity<Object> createNewAccount(@Valid @RequestBody NewAccount account) throws MessagingException {
        accountService.createAccountByAdmin(account);
        return ResponseBuilder.returnMessage(HttpStatus.CREATED, "Create new account successfully");
    }

}
