package org.example.userservicespring.dto;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "users")
@Getter
public class UserModel extends RepresentationModel<UserModel> {
    private final Long id;
    private final String username;
    private final String email;
    private final Integer age;

    public UserModel(UserResponse userResponse) {
        this.id = userResponse.getId();
        this.username = userResponse.getName();
        this.email = userResponse.getEmail();
        this.age = userResponse.getAge();
    }
}
