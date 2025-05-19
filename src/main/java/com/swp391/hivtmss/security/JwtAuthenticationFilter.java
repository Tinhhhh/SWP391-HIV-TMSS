package com.swp391.hivtmss.security;

import com.swp391.hivtmss.model.entity.AccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetail;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //get jwt token from header
        String jwt = jwtTokenProvider.getJwtFromRequest(request);

        if (jwtTokenProvider.isRefreshToken(jwt)) {
            // If the token is a refresh token, skip authentication
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            //get username from jwt token
            String username = jwtTokenProvider.getUsernameFromJwt(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetail.loadUserByUsername(username);

                AccessToken token = jwtTokenProvider.getTokenFromJwt(jwt);

                if (jwtTokenProvider.isTokenValid(jwt, userDetails.getUsername())
                        && token != null
                        && !jwtTokenProvider.isTokenRevoked(token.getUserId(), token.getJwtId())
                        && !jwtTokenProvider.isTokenExpired(jwt)
                ) {
                    UsernamePasswordAuthenticationToken authenToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authenToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }


}
