package com.swp391.hivtmss.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swp391.hivtmss.model.entity.AccessToken;
import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.RefreshToken;
import com.swp391.hivtmss.model.entity.Role;
import com.swp391.hivtmss.model.payload.enums.EmailTemplateName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.RegisterAccountExistedException;
import com.swp391.hivtmss.model.payload.request.AuthenticationRequest;
import com.swp391.hivtmss.model.payload.request.RegistrationRequest;
import com.swp391.hivtmss.model.payload.response.AuthenticationResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.RefreshTokenRepository;
import com.swp391.hivtmss.repository.RoleRepository;
import com.swp391.hivtmss.security.JwtTokenProvider;
import com.swp391.hivtmss.service.AuthenService;
import com.swp391.hivtmss.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Value("${application.email.url}")
    private String forgotPasswordUrl;

    @Value("${application.email.secure.characters}")
    private String emailSecureChar;

    @Override
    public void register(RegistrationRequest request) throws MessagingException {

        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegisterAccountExistedException("Email đã tồn tại, vui lòng sử dụng email khác");
        }

        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "RoleName not found"));

        String password = passwordEncoder.encode(request.getPassword());
        Account account = modelMapper.map(request, Account.class);
        account.setLocked(false);
        account.setActive(true);
        account.setPassword(password);
        account.setRole(role);

        accountRepository.save(account);
        sendRegistrationEmail(account, request.getPassword());
    }

    private void sendRegistrationEmail(Account account, String password) throws MessagingException {
        emailService.sendAccountInformation(
                account.fullName(), account.getEmail(), password, account.getEmail(),
                EmailTemplateName.CREATE_ACCOUNT.getName(), "[HIV TMSS service] Thông tin tài khoản của bạn đã đăng ký, cảm ơn đã sử dụng hệ thống của chúng tôi");
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new HivtmssException(HttpStatus.UNAUTHORIZED, "Authentication fails, your email is not exist"));

        if (account.isLocked()) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "Tài khoản của bạn đã bị khóa, vui lòng liên hệ quản trị viên để được hỗ trợ");
        }

        //Create new token
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Save new token
        saveRefreshToken(accessToken, refreshToken);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshToken(String accessToken, String refreshToken) {
        AccessToken token = jwtTokenProvider.getTokenFromJwt(accessToken);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .revoked(false)
                .expired(false)
                .jitId(token.getJwtId())
                .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "No jwt Token found in the request header");
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenProvider.getUsernameFromJwt(refreshToken);

        //validate
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Token is invalid or not exist"));

            if (!oldRefreshToken.isRevoked() && !oldRefreshToken.isExpired()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                Account account = accountRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Invalid user. User not found"));

                //Revoke old tokens
                jwtTokenProvider.revokeAccessToken(account.getId(), oldRefreshToken.getJitId());

                oldRefreshToken.setRevoked(true);
                oldRefreshToken.setExpired(true);
                refreshTokenRepository.save(oldRefreshToken);

                //generate new token
                String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

                saveRefreshToken(newAccessToken, newRefreshToken);

                return AuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();

            } else {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Token is invalid or not exist");
            }
        }

        return null;
    }

    @Override
    public void forgotPassword(String email) throws NoSuchAlgorithmException, MessagingException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        String token = generateResetPasswordToken(6);
//        StringBuilder link = new StringBuilder();
//        link.append(forgotPasswordUrl).append("/").append(token);
        emailService.sendMimeMessageWithHtml(
                account.fullName(),
                account.getEmail(),
                token,
                EmailTemplateName.FORGOT_PASSWORD.getName(),
                "Your recovery code");

        jwtTokenProvider.savePasswordResetToken(token, account.getId());
    }

    @Override
    public void resetPassword(String email, String newPassword, String code) throws JsonProcessingException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        if (!jwtTokenProvider.isExistsPasswordResetToken(account.getId(), code)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Token is expired or not exist");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        jwtTokenProvider.deletePasswordResetToken(account.getId(), code);
        jwtTokenProvider.disableOldTokens(account.getId());
    }

    private String generateResetPasswordToken(int codelength) throws NoSuchAlgorithmException {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        //lay ngau nhien 1 ki tu trong chuoi emailSecurecHar
        for (int i = 0; i < codelength; i++) {
            int randomIndex = random.nextInt(emailSecureChar.length());
            codeBuilder.append(emailSecureChar.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

}
