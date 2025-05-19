package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.response.AccountInfoResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.security.JwtTokenProvider;
import com.swp391.hivtmss.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Override
    public void changePassword(HttpServletRequest request, String oldPassword, String newPassword) {

        String email = extractEmail(request);

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        //Tạo 1 token mới, làm cột mốc revoke các token cũ
        jwtTokenProvider.disableOldTokens(account.getAccountId());
    }

    private String extractEmail(HttpServletRequest request) {
        String jwt = jwtTokenProvider.getJwtFromRequest(request);

        if (jwtTokenProvider.isRefreshToken(jwt)) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "Token is invalid");
        }

        return jwtTokenProvider.getUsernameFromJwt(jwt);
    }

    @Override
    public AccountInfoResponse getCurrentAccountInfo(HttpServletRequest request) {

        String email = extractEmail(request);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        AccountInfoResponse accountInfoResponse = modelMapper.map(account, AccountInfoResponse.class);
        accountInfoResponse.setRoleName(account.getRole().getRoleName());
        return accountInfoResponse;
    }

    @Override
    public void updateAccountInfo(UUID id, EditAccount account) {

    }

    @Override
    public void editAccountByAdmin(UUID accountId, EditAccountByAdmin account) {

    }
}
