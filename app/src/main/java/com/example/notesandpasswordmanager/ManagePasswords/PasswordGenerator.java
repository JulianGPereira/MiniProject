package com.example.notesandpasswordmanager.ManagePasswords;

import java.util.Random;

public class PasswordGenerator {
    public static String generatePassword(int length) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$%^&*()_?/";
        String numbers = "0123456789";
        String combinedChars = upperCase + lowerCase+ specialCharacters + numbers;
        Random random = new Random();
        StringBuilder password=new StringBuilder();
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        for(int i = 4; i< length ; i++) {
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }
        return password.toString();
    }
}
