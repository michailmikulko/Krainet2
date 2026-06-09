package com.authService.controller;

import com.authService.dto.request.CreateUserRequest;
import com.authService.dto.request.UpdateUserRequest;
import com.authService.dto.response.UserResponse;
import com.authService.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long id
            ){
        log.info("Called getUserById :id = "+id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
            ){
        log.info("Called createUsers");
        return ResponseEntity.status(201)
                .body(userService.createUser(request));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        log.info("Called updateUser id = {}, userToUpdate  = {}",id,request);
        var updated = userService.updateUser(id,request);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteUser id = "+id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();

    }

}
