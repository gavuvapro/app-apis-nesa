package com.nesa.app_apis.service;

import com.nesa.app_apis.dto.AuthResponse;
import com.nesa.app_apis.dto.LoginRequest;
import com.nesa.app_apis.dto.RegisterRequest;
import com.nesa.app_apis.entity.User;
import com.nesa.app_apis.enums.Role;
import com.nesa.app_apis.repository.UserRepository;
import com.nesa.app_apis.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public String register(RegisterRequest request) {

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .verificationToken(verificationToken)
                .build();

        userRepository.save(user);
        String verifyLink = "http://localhost:8080/api/auth/verify?token=" + verificationToken;

        emailService.sendEmail(
                user.getEmail(),
                "Verify Account",
                "<h1>Verify Your Account</h1><a href='" + verifyLink + "'>Verify</a>"
        );

        return "Registration successful. Check your email.";
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Email not verified");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
    public String verifyAccount(String token) {

        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        return "Account verified successfully";
    }

    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();

        user.setResetToken(resetToken);

        userRepository.save(user);
        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;

        emailService.sendEmail(
                email,
                "Reset Password",
                "<h1>Reset Password</h1><a href='" + resetLink + "'>Reset</a>"
        );

        return "Password reset email sent";
    }

    public String resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        userRepository.save(user);

        return "Password updated successfully";
    }
}