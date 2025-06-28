package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.enums.SortByRole;
import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.request.NewAccount;
import com.swp391.hivtmss.model.payload.response.AccountInfoResponse;
import com.swp391.hivtmss.model.payload.response.AccountReponseForAdmin;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AccountService {
    void changePassword(HttpServletRequest email, String oldPassword, String newPassword);

    AccountInfoResponse getCurrentAccountInfo(HttpServletRequest request);

    void updateAccountInfo(HttpServletRequest request, EditAccount account);

    void editAccountByAdmin(UUID accountId, EditAccountByAdmin account);

    AccountReponseForAdmin getAccountForAdmin(UUID accountId);

    ListResponse getAccountsForAdmin(int pageNo, int pageSize, String keyword, String sortBy, String sortDir, SortByRole role);

    void createAccountByAdmin(NewAccount account) throws MessagingException;

    AccountInfoResponse uploadAvatar(HttpServletRequest request, MultipartFile file);
}