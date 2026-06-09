package com.testtask.krainet;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record User(

        @Null
        Long id,
        @NotBlank
        String username,
        @NotBlank
        String password,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        RoleType role
)
{
}
