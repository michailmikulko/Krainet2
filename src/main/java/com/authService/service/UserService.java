package com.authService.service;

import com.authService.dto.mapping.UserMapper;
import com.authService.dto.request.CreateUserRequest;
import com.authService.dto.request.UpdateUserRequest;
import com.authService.dto.response.UserResponse;
import com.authService.krainet.RoleType;
import com.authService.entity.UserEntity;
import com.authService.krainet.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {


    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
                request.getPassword(),
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
}
