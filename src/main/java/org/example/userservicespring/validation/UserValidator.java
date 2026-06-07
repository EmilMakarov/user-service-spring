package org.example.userservicespring.validation;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public String emailValidation(String email) {
        String trimmed = email.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!trimmed.contains("@") || trimmed.indexOf("@") > trimmed.lastIndexOf('.')) {
            throw new IllegalArgumentException("Некорректный email");
        }
        return trimmed;
    }

    public void nameValidation(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!name.trim().matches("^[\\p{L} \\-']+$") || name.trim().length() < 3) {
            throw new IllegalArgumentException("Некорректное имя");
        }
    }

    public void ageValidation(int age) {
        if (age < 1 || age >= 120) {
            throw new IllegalArgumentException("Некорректный возраст");
        }
    }
}
