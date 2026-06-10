package com.authService.dto.jwt;

import lombok.Data;

@Data
public class JwtAuthentificationDto {
    private String token;
    private String refreshToken;
}
