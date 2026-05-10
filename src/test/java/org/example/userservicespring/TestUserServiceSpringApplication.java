package org.example.userservicespring;

import org.springframework.boot.SpringApplication;

public class TestUserServiceSpringApplication {

    public static void main(String[] args) {
        SpringApplication.from(UserServiceSpringApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
