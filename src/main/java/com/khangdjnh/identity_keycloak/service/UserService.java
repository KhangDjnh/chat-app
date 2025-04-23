package com.khangdjnh.identity_keycloak.service;

import com.khangdjnh.identity_keycloak.dto.request.UserCreateRequest;
import com.khangdjnh.identity_keycloak.dto.request.UserUpdateRequest;
import com.khangdjnh.identity_keycloak.dto.response.UserResponse;
import com.khangdjnh.identity_keycloak.entity.User;
import com.khangdjnh.identity_keycloak.identity.Credential;
import com.khangdjnh.identity_keycloak.identity.TokenExchangeParam;
import com.khangdjnh.identity_keycloak.identity.UserCreationParam;
import com.khangdjnh.identity_keycloak.mapper.UserMapper;
import com.khangdjnh.identity_keycloak.repository.IdentityClient;
import com.khangdjnh.identity_keycloak.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    IdentityClient identityClient;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    private String extractUserId(ResponseEntity<?> responseEntity) {
        String location = Objects.requireNonNull(responseEntity.getHeaders().get("Location")).getFirst();
        String[] splittedString = location.split("/");
        return splittedString[splittedString.length - 1];
    }

    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());
        var creationResponse = identityClient.createUser(
                "Bearer " + token.getAccessToken(),
                UserCreationParam.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .enabled(true)
                        .emailVerified(false)
                        .credentials(List.of(Credential.builder()
                                .type("password")
                                .value(request.getPassword())
                                .temporary(false)
                                .build()))
                        .build());
        //Khi goi toi api createUser de tao User tren Keycloak thi tra ve Header.Location co chua userId, ta can lay no dua vao db
        String userKeycloakId = extractUserId(creationResponse);
        User user = userMapper.toUser(request);
        user.setUserKeycloakId(userKeycloakId);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserById(String userId) {
        return userRepository.findById(userId).map(userMapper::toUserResponse).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userRepository.findByUserId(userId);
        userMapper.updateUserFromRequest(user, request);
        String decodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(decodedPassword);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
