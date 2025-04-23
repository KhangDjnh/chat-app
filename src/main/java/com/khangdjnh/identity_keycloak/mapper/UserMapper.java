package com.khangdjnh.identity_keycloak.mapper;

import com.khangdjnh.identity_keycloak.dto.request.UserCreateRequest;
import com.khangdjnh.identity_keycloak.dto.request.UserUpdateRequest;
import com.khangdjnh.identity_keycloak.dto.response.UserResponse;
import com.khangdjnh.identity_keycloak.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.lang.annotation.Target;
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(@MappingTarget User user, UserUpdateRequest request);
}
