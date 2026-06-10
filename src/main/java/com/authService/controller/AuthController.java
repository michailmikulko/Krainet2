package com.authService.controller;

import com.authService.dto.jwt.JwtAuthentificationDto;
import com.authService.dto.jwt.RefreshTokenDto;
import com.authService.dto.jwt.UserCredentialsDto;
import com.authService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthentificationDto> signIn(@RequestBody UserCredentialsDto userCredentialsDto) throws org.apache.tomcat.websocket.AuthenticationException {
        try{
            JwtAuthentificationDto jwtAuthentificationDto = userService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthentificationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed"+e.getMessage());
        }
    }
    @PostMapping("/refresh")
    public JwtAuthentificationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
