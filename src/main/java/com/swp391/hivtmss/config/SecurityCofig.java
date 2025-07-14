package com.swp391.hivtmss.config;

import com.swp391.hivtmss.security.CustomLogoutHandler;
import com.swp391.hivtmss.security.JwtAuthenticationEntryPoint;
import com.swp391.hivtmss.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityCofig {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter authenticationFilter;

    private final CustomLogoutHandler logoutHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(request ->
                        //Authenticate
                        request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/**").permitAll()
                                //Account
                                .requestMatchers(HttpMethod.GET, "/api/v1/accounts").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/accounts").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER", "ADMIN")
                                .requestMatchers(
                                        "/api/v1/accounts/change-password",
                                        "/api/v1/accounts/avatar").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER", "ADMIN")
                                .requestMatchers("/api/v1/accounts/admin/**").hasAuthority("ADMIN")
                                //Appointment
                                .requestMatchers(HttpMethod.GET, "/api/v1/appointments").hasAnyAuthority("CUSTOMER", "DOCTOR", "ADMIN")
                                .requestMatchers(
                                        "/api/v1/appointments/by-range",
                                        "/api/v1/appointments/all").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers("/api/v1/appointments/available-doctors").hasAnyAuthority("DOCTOR", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/appointments").hasAuthority("CUSTOMER")
                                .requestMatchers
                                        ("/api/v1/appointments/cancel",
                                                "/api/v1/appointments/customer").hasAuthority("CUSTOMER")
                                .requestMatchers(
                                        "/api/v1/appointments/treatment",
                                        "/api/v1/appointments/diagnosis").hasAuthority("DOCTOR")
                                .requestMatchers(
                                        "/api/v1/appointments/dashboard",
                                        "/api/v1/appointments/dashboard/monthly").hasAuthority("ADMIN")
                                //Treatment-regimen
                                .requestMatchers(
                                        "/api/v1/treatment-regimens/list",
                                        "/api/v1/treatment-regimens/detail").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/treatment-regimens").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/treatment-regimens").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/treatment-regimens").hasAuthority("ADMIN")
                                //Treatment-regimen-drugs
                                .requestMatchers(HttpMethod.GET,
                                        "/api/v1/treatment-regimen-drugs",
                                        "/api/v1/treatment-regimen-drugs/drug",
                                        "/api/v1/treatment-regimen-drugs/regimen").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/treatment-regimen-drugs").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/treatment-regimen-drugs").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/treatment-regimen-drugs").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/treatment-regimen-drugs/activate").hasAuthority("ADMIN")
                                //Test-type
                                .requestMatchers(HttpMethod.GET, "/api/v1/test-types", "/api/v1/test-types/all").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/test-types").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/test-types").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/test-types").hasAuthority("ADMIN")
                                //Notification
                                .requestMatchers("/api/v1/notifications/**").permitAll()
                                //Drug
                                .requestMatchers(HttpMethod.GET, "/api/v1/drugs", "/api/v1/drugs/all").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/drugs").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/drugs").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/drugs").hasAuthority("ADMIN")
                                //Doctor-degrees
                                .requestMatchers("/api/v1/doctor-degrees/**").hasAnyAuthority("DOCTOR", "ADMIN")
                                //Diagnosis
                                .requestMatchers("/api/v1/diagnosis/**").hasAnyAuthority("DOCTOR", "ADMIN")
                                //Blog
                                .requestMatchers(HttpMethod.GET,
                                        "/api/v1/blogs",
                                        "/api/v1/blogs/all",
                                        "api/v1/blogs/blog/all",
                                        "api/v1/blogs/account").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/blogs").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER")
                                .requestMatchers(HttpMethod.POST,
                                        "/api/v1/blogs",
                                        "/api/v1/blogs/images").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER")
                                .requestMatchers(HttpMethod.DELETE,
                                        "/api/v1/blogs",
                                        "/api/v1/blogs/images",
                                        "/api/v1/blogs/images/by-url").hasAnyAuthority("CUSTOMER", "DOCTOR", "MANAGER")
                                .requestMatchers(
                                        "/api/v1/blogs/updateStatus",
                                        "/api/v1/blogs/rejected").hasAuthority("MANAGER")
                                //appointment-changes
                                .requestMatchers(HttpMethod.GET, "/api/v1/appointment-changes/**").hasAnyAuthority("DOCTOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/appointment-changes").hasAnyAuthority("DOCTOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/appointment-changes").hasAnyAuthority("DOCTOR")
                                //treatments
                                .requestMatchers("/api/v1/treatments/**").hasAnyAuthority("DOCTOR", "ADMIN", "CUSTOMER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint));

//        http.logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler((request, response, authentication)
//                        -> SecurityContextHolder.clearContext()));

        return http.build();
    }


}
