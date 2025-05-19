package com.swp391.hivtmss.security;

import com.swp391.hivtmss.model.entity.AccessToken;
import com.swp391.hivtmss.model.entity.RefreshToken;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "No jwt token found in header");
        }

        //Terminate current accessToken and refreshToken
        jwt = authHeader.substring(7);
        AccessToken token = jwtTokenProvider.getTokenFromJwt(jwt);
        RefreshToken refreshToken = refreshTokenRepository.findByJitId(token.getJwtId());
        if (refreshToken != null) {
            refreshToken.setExpired(true);
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        }
        jwtTokenProvider.revokeAccessToken(token.getUserId(), token.getJwtId());

        SecurityContextHolder.clearContext();
    }
}
