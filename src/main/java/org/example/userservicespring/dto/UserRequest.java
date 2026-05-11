package org.example.userservicespring.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private int age;
}
