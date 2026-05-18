package com.ws101.Ampuan.Comia.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/v1/auth/register", "/login")
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/script.js", "/style.css", "/*.jpg").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/products").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login.html")
                        .permitAll()
                );

        return http.build();
    }
}