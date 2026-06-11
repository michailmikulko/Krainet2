package com.authService.controller;

import com.authService.dto.request.CreateUserRequest;
import com.authService.dto.request.UpdateMeRequest;
import com.authService.dto.request.UpdateUserRequest;
import com.authService.dto.response.UserResponse;
import com.authService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    @PreAuthorize("#id == authentication.principal.user.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long id
            ){
        log.info("Called getUserById :id = "+id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PostMapping("/registration")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
            ){
        log.info("Called createUsers");
        return ResponseEntity.status(201)
                .body(userService.createUser(request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        log.info("Called updateUser id = {}, userToUpdate  = {}",id,request);
        var updated = userService.updateUser(id,request);
        return ResponseEntity.ok(updated);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            Authentication authentication,
            @RequestBody @Valid UpdateMeRequest request
    ) {
        log.info("Called updateUser");
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateMe(email, request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteUser id = "+id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();

    }

}
