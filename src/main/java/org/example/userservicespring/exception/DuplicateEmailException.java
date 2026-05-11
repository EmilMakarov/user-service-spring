package org.example.userservicespring.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}
