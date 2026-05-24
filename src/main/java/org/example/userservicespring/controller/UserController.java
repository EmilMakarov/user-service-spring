package org.example.userservicespring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservicespring.dto.UserModel;
import org.example.userservicespring.dto.UserRequest;
import org.example.userservicespring.dto.UserResponse;
import org.example.userservicespring.dto.UserUpdateRequest;
import org.example.userservicespring.service.UserModelAssembler;
import org.example.userservicespring.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<UserModel>> createUser(@RequestBody UserRequest request) {
        log.info("Запрос на создание пользователя");
        UserResponse response = userService.createUser(request);
        UserModel userModel = userModelAssembler.toModel(response);
        log.debug("Ответ от сервера: {}", response.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(userModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserModel>> getUser(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id: {}", id);
        UserResponse response = userService.getUserById(id);
        UserModel userModel = userModelAssembler.toModel(response);
        log.debug("Ответ сервера: {}", response.toString());
        return ResponseEntity.ok(EntityModel.of(userModel));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserModel>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        log.info("Запрос на изменение пользователя с id: {}", id);
        UserResponse response = userService.updateUser(id, request);
        UserModel userModel = userModelAssembler.toModel(response);
        return ResponseEntity.ok(EntityModel.of(userModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
