package com.ws101.Ampuan.Comia.EcommerceApi.controller;

import com.ws101.Ampuan.Comia.EcommerceApi.dto.RegisterUserDto;
import com.ws101.Ampuan.Comia.EcommerceApi.model.User;
import com.ws101.Ampuan.Comia.EcommerceApi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto dto) {
        // I-verify kung umiiral na ang username
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // I-map ang DTO data papunta sa totoong User model object
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password())); // Hashing
        user.setRole(dto.role());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}