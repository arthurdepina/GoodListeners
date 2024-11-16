package com.goodlisteners.service;

import com.goodlisteners.exception.BusinessException;
import com.goodlisteners.repository.UserRepository;
import com.goodlisteners.user.model.User;
import com.goodlisteners.user.model.dto.UserRequestDto;
import com.goodlisteners.user.model.dto.UserResponseDto;
import com.goodlisteners.utils.PasswordGeneratorUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JavaMailSender mailSender, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public void createUser(UserRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());

        if (user.isPresent()) {
            throw new BusinessException("Email already registered!", "001",HttpStatus.CONFLICT);
        }

        User userEntity = new User();
        userEntity.setName(requestDto.getName());
        userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userEntity.setEmail(requestDto.getEmail());

        userRepository.save(userEntity);
    }

    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new
                        BusinessException("User not found with email: " + email, "050", HttpStatus.NOT_FOUND));

        return UserResponseDto.fromEntity(user);
    }

    public UserResponseDto findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new
                        BusinessException("User not found to id: " + id, "051", HttpStatus.NOT_FOUND));

        return UserResponseDto.fromEntity(user);
    }

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new
                        BusinessException("User not found with email: " + email, "050", HttpStatus.NOT_FOUND));

        String newPassword = PasswordGeneratorUtil.generateRandomPassword(12);

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        try {
            sendPasswordResetEmail(email, newPassword);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BusinessException("Failed to send email to: " + email, "070", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendPasswordResetEmail(String email, String newPassword) throws MessagingException, UnsupportedEncodingException {

        String subject = "Sua nova senha";

        String body = "<p>Olá,</p>" +
                "<p>Sua nova senha foi gerada com sucesso. Use a senha abaixo para acessar:</p>" +
                "<p><b>" + newPassword + "</b></p>" +
                "<p>Por segurança, recomendamos que você altere sua senha após fazer login.</p>" +
                "<p>Atenciosamente,<br>GoodListeners</p>";

        emailService.sendEmail(email, subject, body);
    }
}
