package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Role;
import com.swp391.hivtmss.model.payload.enums.EmailTemplateName;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.enums.SortByRole;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.RegisterAccountExistedException;
import com.swp391.hivtmss.model.payload.request.EditAccount;
import com.swp391.hivtmss.model.payload.request.EditAccountByAdmin;
import com.swp391.hivtmss.model.payload.request.NewAccount;
import com.swp391.hivtmss.model.payload.response.AccountInfoResponse;
import com.swp391.hivtmss.model.payload.response.AccountReponseForAdmin;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.RoleRepository;
import com.swp391.hivtmss.security.JwtTokenProvider;
import com.swp391.hivtmss.service.AccountService;
import com.swp391.hivtmss.service.CloudinaryService;
import com.swp391.hivtmss.service.EmailService;
import com.swp391.hivtmss.util.AccountSpecification;
import com.swp391.hivtmss.util.DateUtil;
import jakarta.mail.MessagingException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;

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

    @Override
    public void createAccountByAdmin(NewAccount newAcc) throws MessagingException {
        if (accountRepository.findByEmail(newAcc.getEmail()).isPresent()) {
            throw new RegisterAccountExistedException("Account already exists");
        }

        if (!newAcc.getRoleName().equals(RoleName.DOCTOR) && !newAcc.getRoleName().equals(RoleName.MANAGER)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Invalid role name");
        }

        Role role = roleRepository.findByRoleName(newAcc.getRoleName().toString())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Role not found"));

        String password = newAcc.getFirstName() + newAcc.getLastName().trim();
        String encodedPassword = passwordEncoder.encode(password);
        Account account = modelMapper.map(newAcc, Account.class);
        account.setLocked(false);
        account.setPassword(encodedPassword);
        account.setRole(role);

        if (newAcc.getRoleName().equals(RoleName.DOCTOR)) {
            account.setActive(false);
        } else if (newAcc.getRoleName().equals(RoleName.MANAGER)) {
            account.setActive(true);
        }

        accountRepository.save(account);
        sendRegistrationEmail(account, password);
    }

    private void sendRegistrationEmail(Account account, String password) throws MessagingException {
        emailService.sendAccountInformation(
                account.fullName(), account.getEmail(), password, account.getEmail(),
                EmailTemplateName.CREATE_ACCOUNT.getName(), "[HIV TMSS service] Thông tin tài khoản của bạn đã đăng ký, cảm ơn đã sử dụng hệ thống của chúng tôi");
    }

    @Override
    public AccountInfoResponse uploadAvatar(HttpServletRequest request, MultipartFile file) {
        try {
            String email = extractEmail(request);
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

            // Delete old avatar if exists
            if (account.getAvatar() != null && !account.getAvatar().isEmpty()) {
                try {
                    cloudinaryService.deleteFile(account.getAvatar());
                } catch (IOException e) {
                    // Log error but continue with upload
                    System.err.println("Failed to delete old avatar: " + e.getMessage());
                }
            }

            // Upload new avatar
            String avatarUrl = cloudinaryService.uploadFile(file);
            account.setAvatar(avatarUrl);
            accountRepository.save(account);

            // Return updated account info
            AccountInfoResponse accountInfoResponse = modelMapper.map(account, AccountInfoResponse.class);
            accountInfoResponse.setRoleName(account.getRole().getRoleName());
            return accountInfoResponse;
        } catch (IOException e) {
            throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload avatar: " + e.getMessage());
        }
    }
}
