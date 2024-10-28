package com.goodlisteners.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.goodlisteners.main",
    "com.goodlisteners.controllers",
    "com.goodlisteners.handlers",
    "com.goodlisteners.service",
    "com.goodlisteners.repository"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}