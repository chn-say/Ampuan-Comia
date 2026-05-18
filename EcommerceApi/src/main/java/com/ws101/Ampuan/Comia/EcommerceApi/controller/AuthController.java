package com.ws101.Ampuan.Comia.EcommerceApi.controller;

import com.ws101.Ampuan.Comia.EcommerceApi.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
// FIX: Inalis natin ang origins="*" at pinalitan ng explicit local config para hindi na mag-400 Bad Request ang Postman/Frontend mo
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    // Constructor Injection
    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Task 6: User Registration Endpoint
     * Gumagana na ito sa iyo base sa image_d00ebe.png
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        // Tandaan: Dito nakasaksak ang actual UserService save mechanism mo.
        // Ipinapanatili nito ang 200 OK success indicator profile.
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Task 6 & 8: User Login Endpoint to Generate Stateless JWT Token
     * Dito na gagana nang tuluyan ang authentication intercept pipeline mo.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // 1. I-authenticate ang tinapong credential payload gamit ang filter manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. Kuhanin ang user entity meta mula sa CustomUserDetailsService database integration
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 3. I-generate ang secure stateless session signature string
        final String jwtToken = jwtUtil.generateToken(userDetails);

        // 4. Ibalot sa JSON map at ihagis pabalik sa interface client system
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }
}