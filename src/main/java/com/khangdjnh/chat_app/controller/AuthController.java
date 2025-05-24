package com.khangdjnh.chat_app.controller;

import com.khangdjnh.chat_app.dto.request.LoginRequest;
import com.khangdjnh.chat_app.dto.request.UserCreateRequest;
import com.khangdjnh.chat_app.dto.response.ApiResponse;
import com.khangdjnh.chat_app.dto.response.LoginResponse;
import com.khangdjnh.chat_app.dto.response.UserResponse;
import com.khangdjnh.chat_app.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    UserService userService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(1000)
                .message("Success")
                .result(userService.login(request))
                .build();
    }
    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Success")
                .result(userService.createUser(request))
                .build();
    }

}
