package org.example.userservicespring.dto;

import lombok.Data;

@Data
public class UserRequest {
    String name;
    String email;
    int age;
}
