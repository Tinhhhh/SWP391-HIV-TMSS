package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.response.AccountInfoResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface AccountService {
    void changePassword(HttpServletRequest email, String oldPassword, String newPassword);

    AccountInfoResponse getCurrentAccountInfo(HttpServletRequest request);

    void updateAccountInfo(UUID id, EditAccount account);

    void editAccountByAdmin(UUID accountId, EditAccountByAdmin account);
}