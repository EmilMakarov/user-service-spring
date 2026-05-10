package org.example.userservicespring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserUpdate {
    private Long id;
    private String name;
    private String email;
    private int age;
    private LocalDate createdAt;
}
