package com.khangdjnh.identity_keycloak.service;

import com.khangdjnh.identity_keycloak.dto.request.UserCreateRequest;
import com.khangdjnh.identity_keycloak.dto.request.UserUpdateRequest;
import com.khangdjnh.identity_keycloak.dto.response.UserResponse;
import com.khangdjnh.identity_keycloak.entity.User;
import com.khangdjnh.identity_keycloak.mapper.UserMapper;
import com.khangdjnh.identity_keycloak.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user =  userMapper.toUser(request);
        String decodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(decodedPassword);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers () {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    public UserResponse getUserById (String userId) {
        return userRepository.findById(userId).map(userMapper::toUserResponse).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    public UserResponse updateUser (String userId, UserUpdateRequest request) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userRepository.findByUserId(userId);
        userMapper.updateUserFromRequest(user, request);
        String decodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(decodedPassword);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    public void deleteUser (String userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
