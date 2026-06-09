package com.testtask.controller;

import com.testtask.krainet.User;
import com.testtask.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("id") Long id
            ){
        log.info("Called getUserById :id = "+id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PostMapping()
    public ResponseEntity<User> createUser(
            @RequestBody @Valid User userToCrate
            ){
        log.info("Called createUsers");
        return ResponseEntity.status(201)
                .body(userService.createUsers(userToCrate));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid User userToUpdate
    ) {
        log.info("Called updateUser id = {}, userToUpdate  = {}",id,userToUpdate);
        var updated = userService.updateUsers(id,userToUpdate);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteUser id = "+id);
        userService.deleteUsers(id);
        return ResponseEntity.ok().build();

    }

}
