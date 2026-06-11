package com.authService.dto.request;

public record UpdateMeRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        String password
) {}