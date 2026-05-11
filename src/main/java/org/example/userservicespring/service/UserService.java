package org.example.userservicespring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservicespring.dto.UserRequest;
import org.example.userservicespring.dto.UserResponse;
import org.example.userservicespring.dto.UserUpdateRequest;
import org.example.userservicespring.entity.User;
import org.example.userservicespring.exception.DuplicateEmailException;
import org.example.userservicespring.exception.UserNotFoundException;
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
        log.debug("Создание пользователя");
        nameValidation(userRequest.getName());
        emailValidation(userRequest.getEmail());
        ageValidation(userRequest.getAge());
        alreadyExistsEmail(userRequest.getEmail());
        User user = new User();
        user = userRepository.save(user);
        log.info("Пользователь с id {} создан", user.getId());
        return response(user);
    }

    public UserResponse getUserById(Long id) {
        log.debug("Поиск пользователя по id {}", id);
        User user = existById(id);

        return response(user);
    }

    public List<UserResponse> getAllUsers() {
        log.debug("Отображение всех пользователей");
        return userRepository
                .findAll()
                .stream()
                .map(this::response)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest userRequest) {
        log.debug("Обновление информации о пользователе с id {}", id);
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
        log.info("Пользователь с id {} обновлён", id);
        return response(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.debug("Удаление пользователя с id {}", id);
        existById(id);
        userRepository.deleteById(id);
        log.info("Пользователь с id {} удалён", id);
    }

    private String emailValidation(String email) {
        String trimmed = email.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!trimmed.contains("@") || trimmed.indexOf("@") > trimmed.lastIndexOf('.')) {
            throw new IllegalArgumentException("Некорректный email");
        }
        return trimmed;
    }

    private void nameValidation(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }
        if (!name.trim().matches("^[\\p{L} \\-']+$") || name.trim().length() < 3) {
            throw new IllegalArgumentException("Некорректное имя");
        }
    }

    private void ageValidation(int age) {
        if (age < 1 || age >= 120) {
            throw new IllegalArgumentException("Некорректный возраст");
        }
    }

    private User existById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void alreadyExistsEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException(email);
        }
    }

    private UserResponse response(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }
}
