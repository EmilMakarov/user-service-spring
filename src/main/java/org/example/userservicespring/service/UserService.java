package org.example.userservicespring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservicespring.dto.UserRequest;
import org.example.userservicespring.dto.UserResponse;
import org.example.userservicespring.entity.User;
import org.example.userservicespring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        nameValidation(userRequest.getName());
        emailValidation(userRequest.getEmail());
        ageValidation(userRequest.getAge());
        alreadyExistsEmail(userRequest.getEmail());
        User user = new User();
        user = userRepository.save(user);
        return response(user);
    }

    public UserResponse getUserById(Long id) {
        User user = existById(id);
        return response(user);
    }

    public List<UserResponse> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(this::response)
                .toList();
    }

    @Transactional
    public UserResponse update(Long id, UserRequest userRequest) {
        User user = existById(id);
        if (userRequest.getName() != null) {
            nameValidation(userRequest.getName());
            user.setName(userRequest.getName().trim());
        }
        if (userRequest.getEmail() != null) {
            String newMail = emailValidation(userRequest.getEmail());
            if (user.getEmail().equalsIgnoreCase(newMail)) {
                alreadyExistsEmail(newMail);
            }
            user.setEmail(userRequest.getEmail().trim());
        }
        if (userRequest.getAge() != 0) {
            ageValidation(userRequest.getAge());
            user.setAge(userRequest.getAge());
        }
        return response(user);
    }

    @Transactional
    public void delete(Long id) {
        existById(id);
        userRepository.deleteById(id);
    }

    private String emailValidation(String email) {
        String trimmed = email.trim();
        if (trimmed.isEmpty()) {
            log.warn("Email null или пустая строка");
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!trimmed.contains("@") || trimmed.indexOf("@") > trimmed.lastIndexOf('.')) {
            log.warn("Некорректный email");
            throw new IllegalArgumentException("Некорректный email");
        }
        return trimmed;
    }

    private void nameValidation(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            log.warn("Пустая строка в имени");
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!name.trim().matches("^[\\p{L} \\-']+$") || name.trim().length() < 3) {
            log.warn("Введено некорректное имя");
            throw new IllegalArgumentException("Некорректное имя");
        }
    }

    private void ageValidation(int age) {
        if (age < 1 || age >= 120) {
            log.warn("Введён некорректный возраст");
            throw new IllegalArgumentException("Некорректный возраст");
        }
    }

    private User existById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не найден"));
    }

    private void alreadyExistsEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Пользователь с таким email уже существует {}", email);
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
    }

    private UserResponse response(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }
}
