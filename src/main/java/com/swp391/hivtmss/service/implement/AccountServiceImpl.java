package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.payload.enums.SortByRole;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.response.AccountInfoResponse;
import com.swp391.hivtmss.model.payload.response.AccountReponseForAdmin;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.RoleRepository;
import com.swp391.hivtmss.security.JwtTokenProvider;
import com.swp391.hivtmss.service.AccountService;
import com.swp391.hivtmss.util.AccountSpecification;
import com.swp391.hivtmss.util.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;

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
        jwtTokenProvider.disableOldTokens(account.getId());
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
    public void updateAccountInfo(HttpServletRequest request, EditAccount account) {

        String email = extractEmail(request);
        Account existingAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        modelMapper.map(account, existingAccount);
        accountRepository.save(existingAccount);
    }

    @Override
    public void editAccountByAdmin(UUID accountId, EditAccountByAdmin account) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with ID: " + accountId));

        restrictedModelMapper.map(account, existingAccount);
        existingAccount.setRole(roleRepository.findById(account.getRoleId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found with ID: " + account.getRoleId())));

        accountRepository.save(existingAccount);
    }

    @Override
    public AccountReponseForAdmin getAccountForAdmin(UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with ID: " + accountId));

        AccountReponseForAdmin accountResponse = modelMapper.map(account, AccountReponseForAdmin.class);
        accountResponse.setCreatedDate(DateUtil.formatTimestamp(account.getCreatedDate(), DateUtil.DATE_TIME_FORMAT));
        return accountResponse;
    }

    @Override
    public ListResponse getAccountsForAdmin(int pageNo, int pageSize, String keyword, String sortBy, String sortDir, SortByRole role) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Account> spec;

        if (Objects.equals(role.getValue(), SortByRole.ALL.getValue())) {
            spec = Specification.where(AccountSpecification.hasEmailOrFullName(keyword));
        } else {
            spec = Specification.where(AccountSpecification.hasEmailOrFullName(keyword))
                    .and(AccountSpecification.hasRoleName(role));
        }

        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        List<Account> accountList = accounts.getContent();

        List<AccountReponseForAdmin> accountResponses = accountList.stream()
                .map(account -> {
                    AccountReponseForAdmin acc = modelMapper.map(account, AccountReponseForAdmin.class);
                    acc.setCreatedDate(DateUtil.formatTimestamp(account.getCreatedDate(), DateUtil.DATE_TIME_FORMAT));
                    return acc;
                })
                .toList();

        ListResponse listResponse = new ListResponse();
        listResponse.setPageNo(pageNo);
        listResponse.setPageSize(pageSize);
        listResponse.setTotalElements(accounts.getTotalElements());
        listResponse.setTotalPages(accounts.getTotalPages());
        listResponse.setContent(accountResponses);
        listResponse.setLast(accounts.isLast());

        return listResponse;
    }
}
