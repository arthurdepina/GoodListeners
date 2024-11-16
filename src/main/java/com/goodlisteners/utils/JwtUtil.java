package com.goodlisteners.utils;

import com.goodlisteners.user.model.dto.UserResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Gera uma chave segura para o algoritmo HS512
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437".getBytes());

    public static String generateJwtToken(UserResponseDto user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId())) // ID do usuário como identificador
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expiração: 1 dia
                .signWith(SECRET_KEY) // Usa a chave segura gerada
                .compact();
    }
}

