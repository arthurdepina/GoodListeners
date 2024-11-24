package com.goodlisteners.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        //Essa configuração está pronta para emails do @gmail de forma estática, mas talvez precise configurar dinamicamente o smtp e a porta baseada no domínio do email
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("email.@gmail.com");  //Adicione o email que será o remetente
        mailSender.setPassword("tyrd ptsm ug1j azse"); //Nas configurações da sua conta google crie uma senha de aplicativo e utilize aqui

        mailSender.getJavaMailProperties().put("mail.smtp.auth", "true");
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}