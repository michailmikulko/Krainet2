package com.authService.dto.mapping;

import com.authService.dto.request.CreateUserRequest;
import com.authService.dto.response.UserResponse;
import com.authService.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(CreateUserRequest request);
}