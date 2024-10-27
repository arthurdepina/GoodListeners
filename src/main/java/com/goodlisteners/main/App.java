package com.goodlisteners.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Anotação que inicia o Spring Boot
public class App {
    public static void main(String[] args) {
        // Inicializa o contexto do Spring Boot e inicia o servidor
        SpringApplication.run(App.class, args);
        
        System.out.println(getGreeting());
        System.out.println("Iniciando aplicação...");
    }

    // Método que retornará uma mensagem de saudação
    public static String getGreeting() {
        return "Hello, World!";
    }
}
