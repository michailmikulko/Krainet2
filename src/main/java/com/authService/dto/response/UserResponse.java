package com.authService.dto.response;

import com.authService.entity.RoleType;
import lombok.Data;

@Data
public class UserResponse {
    //private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private RoleType role;
}