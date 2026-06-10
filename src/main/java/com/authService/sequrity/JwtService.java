package com.authService.sequrity;

import com.authService.controller.UserController;
import com.authService.dto.jwt.JwtAuthentificationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("mHy7gwZaoLGn7P6RIiva0zaxAmLK4QQcuQ7E2B/2Azg=")
    private String jwtSecret;

    public JwtAuthentificationDto generateAuthToken(String username){
        JwtAuthentificationDto jwtDto = new JwtAuthentificationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    public JwtAuthentificationDto refreshBaseToken(String username, String refreshToken){
        JwtAuthentificationDto jwtDto = new JwtAuthentificationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();

    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JwtException",e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JwtException",e);
        } catch (MalformedJwtException e) {
            log.error("Malformed JwtException",e);
        } catch (SecurityException e) {
            log.error("Security Exception",e);
        } catch (Exception e) {
            log.error("Invalid token",e);
        }
        return false;
    }

    private String generateJwtToken(String username){
        Date date = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(getSignInKey())
                .compact();
    }
    private String generateRefreshToken(String username){
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(getSignInKey())
                .compact();
    }
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
