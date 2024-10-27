package com.goodlisteners.main;

import com.goodlisteners.user.*;

public class App {
    public static void main(String[] args) {
        System.out.println(getGreeting());

        System.out.println("Iniciando aplicação...");
        
        // Testar a conexão com o banco de dados
        UserRepository userRepository = new UserRepository();
        userRepository.testConnection();
    }

    // Método que retornará uma mensagem de saudação
    public static String getGreeting() {
        return "Hello, World!";
    }
}