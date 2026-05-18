package com.ws101.Ampuan.Comia.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // TAMA: Idinagdag para gumana ang @PreAuthorize sa iyong Controller methods!
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
                        // Bukas para sa lahat ang mga static files at UI pages
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/script.js", "/style.css", "/auth-register.js", "/*.jpg").permitAll()

                        // Bukas para sa lahat ang pagtingin sa mga sapatos at registration endpoint
                        .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()

                        // INAYOS: Role-Based endpoint rules ayon sa Task 3 document requirements
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN") // Admin lang ang pwedeng magbura
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").authenticated() // User-specific: Dapat authenticated para makapag-order

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