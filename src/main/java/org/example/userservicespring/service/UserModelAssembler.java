package org.example.userservicespring.service;

import org.example.userservicespring.controller.UserController;
import org.example.userservicespring.dto.UserModel;
import org.example.userservicespring.dto.UserResponse;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserResponse, UserModel> {
    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(UserResponse entity) {
        UserModel model = new UserModel(entity);
        model.add(linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(entity.getId())).withRel("delete"));
        model.add(linkTo(methodOn(UserController.class).updateUser(entity.getId(), null)).withRel("update"));
        return model;
    }
}
