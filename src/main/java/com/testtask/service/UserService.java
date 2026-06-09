package com.testtask.service;

import com.testtask.krainet.User;
import com.testtask.krainet.UserEntity;
import com.testtask.krainet.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {


    private final UserRepository repository;
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public User getUserById(Long id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user by id = "+id));
        return toDomainUser(userEntity);

    }

    public List<User> findAllUsers() {
        List<UserEntity> allEntities = repository.findAll();
        List<User> userList = allEntities.stream()
                .map(it -> toDomainUser(it)).toList();
        return userList;
    }

    public User createUsers(User userToCrate) {
        if(userToCrate.id()!=null){
            throw new IllegalArgumentException("Id should be empty");

        }
        var entityToSave = new UserEntity(
                null,
                userToCrate.username(),
                userToCrate.password(),
                userToCrate.email(),
                userToCrate.firstName(),
                userToCrate.lastName(),
                userToCrate.role()
        );

        var savedEntity = repository.save(entityToSave);
        return toDomainUser(savedEntity);
    }
    public User updateUsers(Long id, User userToUpdate) {
        var usersEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user by id = "+id));

        var userToSave = new UserEntity(
                usersEntity.getId(),
                userToUpdate.username(),
                userToUpdate.password(),
                userToUpdate.email(),
                userToUpdate.firstName(),
                userToUpdate.lastName(),
                userToUpdate.role()
        );
        var updatedUser = repository.save(userToSave);
        return toDomainUser(updatedUser);
    }

    public void deleteUsers(Long id) {
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Not found user by id = " + id);
        }
        repository.deleteById(id);
    }

    private User toDomainUser(UserEntity users){
        return new User(
                users.getId(),
                users.getUsername(),
                users.getPassword(),
                users.getEmail(),
                users.getFirstName(),
                users.getLastName(),
                users.getRole()
        );
    }
}
