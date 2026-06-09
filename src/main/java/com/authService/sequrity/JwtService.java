package com.authService.sequrity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    @Value("c518b8ca79f858a1ec6447e449047e93d738ffd290ac1f2b128a336bb147d11c")
    private String jwtSecret;
}
