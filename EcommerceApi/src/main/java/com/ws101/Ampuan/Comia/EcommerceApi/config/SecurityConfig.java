package com.ws101.Ampuan.Comia.EcommerceApi.config;

import com.ws101.Ampuan.Comia.EcommerceApi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Pinapanatili nito ang suporta para sa mga @PreAuthorize na role checks mo
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Constructor Injection para sa ating custom filter at user details service
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. I-disable ang CSRF dahil gumagamit na tayo ng stateless JWT tokens (hindi na session cookies)
                .csrf(csrf -> csrf.disable())

                // 2. Gawing ganap na STATELESS ang lifecycle policy ng session store
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. I-configure ang endpoint path access rules
                .authorizeHttpRequests(auth -> auth
                        // Payagan ang lahat ng login, registration, at static assets na ma-access nang walang token
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/login",
                                "/register.html",
                                "/login.html",
                                "/static/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        // Ang lahat ng iba pang request ay nangangailangan ng valid JWT token
                        .anyRequest().authenticated()
                )

                // 4. Isama ang ating custom JWT Authentication Filter bago patakbuhin ang UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 5. Exception handling para sa malinis na JSON 401 Unauthorized footprint
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token missing or invalid");
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}