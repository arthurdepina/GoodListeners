package com.goodlisteners.controllers;

import com.goodlisteners.service.UserService;
import com.goodlisteners.user.model.dto.LoginRequest;
import com.goodlisteners.user.model.dto.UserResponseDto;
import com.goodlisteners.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            UserResponseDto responseDto = userService.findByEmail(loginRequest.getEmail());

            String token = JwtUtil.generateJwtToken(responseDto);

            Map<String, Object> response = new HashMap<>();
            response.put("user", responseDto);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logout realizado com sucesso!");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        userService.resetPassword(email);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Senha alterada com sucesso, verifique seu email!");
        return ResponseEntity.ok(result);
    }
}
