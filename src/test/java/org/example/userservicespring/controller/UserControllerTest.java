package org.example.userservicespring.controller;

import org.example.userservicespring.dto.UserRequest;
import org.example.userservicespring.dto.UserResponse;
import org.example.userservicespring.dto.UserUpdateRequest;
import org.example.userservicespring.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserTest() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Feofan");
        request.setEmail("feofan@gmail.com");
        request.setAge(45);

        UserResponse response = new UserResponse(1L, "Feofan", "feofan@gmail.com", 45, LocalDateTime.now());
        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Feofan"))
                .andExpect(jsonPath("$.email").value("feofan@gmail.com"))
                .andExpect(jsonPath("$.age").value(45));

        verify(userService).createUser(any(UserRequest.class));
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserResponse response = new UserResponse(2L, "Filipp", "kircore@yandex.ru", 60, LocalDateTime.now());
        when(userService.getUserById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Filipp"))
                .andExpect(jsonPath("$.email").value("kircore@yandex.ru"))
                .andExpect(jsonPath("$.age").value(60));

        verify(userService).getUserById(2L);
    }

    @Test
    void getAllUsersTest() throws Exception {
        UserResponse response1 = new UserResponse(1L, "Feofan", "feofan@mail.com", 45, LocalDateTime.now());
        UserResponse response2 = new UserResponse(2L, "Filipp", "kircore@yandex.ru", 60, LocalDateTime.now());
        when(userService.getAllUsers()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Feofan"))
                .andExpect(jsonPath("$[1].name").value("Filipp"));

        verify(userService).getAllUsers();
    }

    @Test
    void updateUserTest() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Akakiy");
        request.setEmail("akakiy@gmail.com");
        request.setAge(88);

        UserResponse response = new UserResponse(1L, "Akakiy", "akakiy@gmail.com", 88, LocalDateTime.now());
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Akakiy"))
                .andExpect(jsonPath("$.email").value("akakiy@gmail.com"))
                .andExpect(jsonPath("$.age").value(88));

        verify(userService).updateUser(eq(1L), any(UserUpdateRequest.class));
    }

    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}