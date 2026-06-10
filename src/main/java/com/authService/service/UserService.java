package com.authService.service;

import com.authService.dto.jwt.JwtAuthentificationDto;
import com.authService.dto.jwt.RefreshTokenDto;
import com.authService.dto.jwt.UserCredentialsDto;
import com.authService.dto.mapping.UserMapper;
import com.authService.dto.request.CreateUserRequest;
import com.authService.dto.request.UpdateUserRequest;
import com.authService.dto.response.UserResponse;
import com.authService.entity.RoleType;
import com.authService.entity.UserEntity;
import com.authService.repository.UserRepository;
import com.authService.sequrity.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    

    private final UserRepository repository;
    private final UserMapper mapper;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserById(Long id) {
        var userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user by id = "+id));
        return mapper.toResponse(userEntity);

    }

    public List<UserResponse> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public UserResponse createUser(CreateUserRequest request) {
        var entityToSave = new UserEntity(
                null,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                RoleType.USER
        );

        var savedEntity = repository.save(entityToSave);
        return mapper.toResponse(savedEntity);
    }
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        UserEntity userToSave = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user by id = " + id));

        userToSave.setUsername(request.username());
        userToSave.setPassword(request.password());
        userToSave.setEmail(request.email());
        userToSave.setFirstName(request.firstName());
        userToSave.setLastName(request.lastName());
        userToSave.setRole(request.role());
        return mapper.toResponse(repository.save(userToSave));
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Not found user by id = " + id);
        }
        repository.deleteById(id);
    }
    public JwtAuthentificationDto signIn(UserCredentialsDto dto) throws AuthenticationException {
        UserEntity userEntity = findByCredentials(dto);
        return jwtService.generateAuthToken(userEntity.getEmail());
    }

    public JwtAuthentificationDto refreshToken(RefreshTokenDto dto) throws Exception {
        String refreshToken = dto.getRefreshToken();
        if(refreshToken!=null && jwtService.validateJwtToken(refreshToken)){
            UserEntity userEntity = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(userEntity.getEmail(),refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    private UserEntity findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<UserEntity> optionalUser = repository.findByEmail(userCredentialsDto.getEmail());
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            if(passwordEncoder.matches(userCredentialsDto.getPassword(),user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    private UserEntity findByEmail(String email) throws Exception{
        return repository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User with email %s not found",email)));
    }
}
