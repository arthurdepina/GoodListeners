package com.goodlisteners.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.goodlisteners.main",
    "com.goodlisteners.controllers",
    "com.goodlisteners.handlers",
    "com.goodlisteners.service",
    "com.goodlisteners.repository",
    "com.goodlisteners.followuser.controller",
    "com.goodlisteners.followuser.service",
    "com.goodlisteners.followuser.repository"
})
 @EntityScan(basePackages = "com.goodlisteners.followuser.model")
 @EnableJpaRepositories(basePackages = "com.goodlisteners.followuser.repository")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}