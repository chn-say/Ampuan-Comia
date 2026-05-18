package com.ws101.Ampuan.Comia.EcommerceApi.security;

import com.ws101.Ampuan.Comia.EcommerceApi.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Kuhanin ang JWT token mula sa Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Kung walang token o hindi nagsisimula sa "Bearer ", hayaan lang dumaan sa filter chain nang hindi authenticated
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Tanggalin ang salitang "Bearer " para makuha ang mismong token string

        try {
            // 2. I-extract ang username gamit ang ating JwtUtil
            username = jwtUtil.extractUsername(jwt);

            // 3. I-verify kung may valid username at kung hindi pa authenticated ang user session sa current thread context
            if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 4. Kung valid ang token, i-set ang security context ng Spring Security
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Opisyal na nating pinapapasok ang user sa secured endpoints
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token verification flow", e);
        }

        // Ipatuloy ang pagpapatakbo sa pipeline filter chain
        filterChain.doFilter(request, response);
    }
}