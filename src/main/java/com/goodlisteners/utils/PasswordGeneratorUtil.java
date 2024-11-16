package com.goodlisteners.utils;

import java.security.SecureRandom;

public class PasswordGeneratorUtil {

    public static String generateRandomPassword(int length) {

        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+";


        SecureRandom random = new SecureRandom();


        StringBuilder password = new StringBuilder();


        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }

        return password.toString();
    }
}
