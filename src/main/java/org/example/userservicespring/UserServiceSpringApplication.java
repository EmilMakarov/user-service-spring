package org.example.userservicespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserServiceSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceSpringApplication.class, args);
    }

}
