package com.swp391.hivtmss.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp391.hivtmss.model.entity.AccessToken;
import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.RefreshToken;
import com.swp391.hivtmss.model.entity.Role;
import com.swp391.hivtmss.model.payload.enums.RedisPrefix;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.RefreshTokenRepository;
import com.swp391.hivtmss.repository.RoleRepository;
import com.swp391.hivtmss.util.DateUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenProvider.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
    private final RoleRepository roleRepository;

    @Value("${app.jwt.secret-key}")
    private String jwtSecret;
    @Value("${app.jwt-access-expiration-milliseconds}")
    private long jwtAccessExpiration;
    @Value("${app.jwt-refresh-expiration-milliseconds}")
    private long jwtRefreshExpiration;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new HivtmssException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty");
        }
    }

    public String generateAccessToken(Authentication authentication) {
        String token = generateAccessToken(authentication, jwtAccessExpiration);
        return token;
    }

    public String generateRefreshToken(Authentication authentication) {
        String token = generateRefreshToken(authentication, jwtRefreshExpiration);
        return token;
    }

    public String generateAccessToken(Authentication authentication, long expiration) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Account not found"));

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .claim("type", "access")
                .claim("id", account.getId().toString())
                .claim("role", authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority())
                .claim("fullName", account.fullName())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String generateRefreshToken(Authentication authentication, long expiration) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Account not found"));

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .claim("type", "refresh")
                .claim("id", account.getId().toString())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String jwt, String username) throws JsonProcessingException {
        //Kiểm tra xem có vừa đổi mật khẩu không
        //Nếu có thì không cho phép sử dụng token cũ
        AccessToken accessToken = getTokenFromJwt(jwt);
        final String key = RedisPrefix.TOKEN_IAT_AVAILABLE.getPrefix() + "_" + accessToken.getUserId();
        Long redisIat = (Long) redisTemplate.opsForValue().get(key);
        if (redisIat != null) {
            LocalDateTime changePasswordTime = Instant.ofEpochMilli(redisIat)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            LocalDateTime tokenTime = DateUtil.convertToLocalDateTime(accessToken.getIssuedAt());

            if (tokenTime.isBefore(changePasswordTime)) {
                return false;
            }

        }

        //Lấy email
        String tokenUserName = getUsernameFromJwt(jwt);
        return tokenUserName.equals(username) && !isTokenExpired(jwt);
    }

    public boolean isTokenExpired(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenRevoked(UUID userId, UUID jwtID) {
        String key = RedisPrefix.TOKEN_BLACKLIST.getPrefix() + "_" + userId + "_" + jwtID;
        return redisTemplate.hasKey(key);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteExpiredOrRevokedTokens() {
        logger.info("Detecting deleting expired tokens task started");

        //Delete expired refreshToken
        logger.info("Deleting expired or revoked refresh tokens");
        List<RefreshToken> expiredRefreshTokens = refreshTokenRepository.findAllByExpiredOrRevoked(true, true);
        logger.info("Found {} expired refresh tokens", expiredRefreshTokens.size());
        refreshTokenRepository.deleteAll(expiredRefreshTokens);
        logger.info("Deleted {} expired refresh tokens", expiredRefreshTokens.size());

    }


    public AccessToken getTokenFromJwt(String jwt) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        if (!RoleName.isExistRole(claims.get("role").toString())) {
            throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found or invalid");
        }

        Role role = roleRepository.findByRoleName(claims.get("role").toString())
                .orElseThrow(() -> new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found"));

        AccessToken token = new AccessToken();
        token.setJwtId(UUID.fromString(claims.getId()));
        token.setUserId(UUID.fromString(claims.get("id").toString()));
        token.setFullName(claims.get("fullName").toString());
        token.setEmail(claims.getSubject());
        token.setRoleId(role.getId());
        token.setIssuedAt(claims.getIssuedAt());
        token.setExpiresAt(claims.getExpiration());

        return token;
    }

    public void revokeAccessToken(UUID userId, UUID jwtId) {
        final Duration ttl = Duration.ofMillis(jwtAccessExpiration);
        final String key = RedisPrefix.TOKEN_BLACKLIST.getPrefix() + "_" + userId + "_" + jwtId;
        if (redisTemplate.hasKey(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, 1, ttl);
    }

    public void savePasswordResetToken(String token, UUID accountId) {
        String key = RedisPrefix.TOKEN_RESET_PASSWORD.getPrefix() + "_" + accountId;
        redisTemplate.opsForValue().set(key, token, Duration.ofMinutes(15));
    }

    public boolean isExistsPasswordResetToken(UUID accountId, String token) throws JsonProcessingException {
        String key = RedisPrefix.TOKEN_RESET_PASSWORD.getPrefix() + "_" + accountId;
        String redisToken = (String) redisTemplate.opsForValue().get(key);
        if (redisToken == null) {
            return false;
        }
        return redisToken.equals(token);
    }

    public void deletePasswordResetToken(UUID accountId, String token) {
        String key = RedisPrefix.TOKEN_RESET_PASSWORD.getPrefix() + "_" + accountId;
        String redisToken = (String) redisTemplate.opsForValue().get(key);
        if (token.equals(redisToken)) {
            redisTemplate.delete(key);
        }
    }

    public boolean isRefreshToken(String jwt) {

        if (jwt == null) {
            return false;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String type = claims.get("type", String.class);
        return type != null && type.equals("refresh");
    }

    public void disableOldTokens(UUID accountId) {
        final Duration ttl = Duration.ofMillis(jwtAccessExpiration);
        final String redisKey = RedisPrefix.TOKEN_IAT_AVAILABLE.getPrefix() + "_" + accountId;
        redisTemplate.opsForValue().set(redisKey, System.currentTimeMillis(), ttl);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Account getAccountFromRequest(HttpServletRequest request) {
        String token = resolveToken(request);
        String email = getUsername(token);
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public String resolveToken(HttpServletRequest request) {
        return getJwtFromRequest(request);
    }

    public String getUsername(String token) {
        return getUsernameFromJwt(token);
    }

}
