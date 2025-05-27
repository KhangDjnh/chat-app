    package com.khangdjnh.chat_app.controller;

    import com.khangdjnh.chat_app.dto.request.ChangePasswordRequest;
    import com.khangdjnh.chat_app.dto.request.UserCreateRequest;
    import com.khangdjnh.chat_app.dto.request.UserUpdateRequest;
    import com.khangdjnh.chat_app.dto.response.ApiResponse;
    import com.khangdjnh.chat_app.dto.response.UserResponse;
    import com.khangdjnh.chat_app.service.UserService;
    import jakarta.validation.Valid;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/users")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    @Slf4j
    public class UserController {
        UserService userService;

        @PostMapping
        ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
            log.info("request: {}", request);
            return ApiResponse.<UserResponse>builder()
                    .message("Success")
                    .code(1000)
                    .result(userService.createUser(request))
                    .build();
        }

        @GetMapping
        ApiResponse<List<UserResponse>> getAllUsers () {
            return ApiResponse.<List<UserResponse>>builder()
                    .code(1000)
                    .message("Success")
                    .result(userService.getAllUsers())
                    .build();
        }

        @GetMapping("/{userId}")
        ApiResponse<UserResponse> getUserById (@PathVariable String userId) {
            return ApiResponse.<UserResponse>builder()
                    .code(1000)
                    .message("Success")
                    .result(userService.getUserById(userId))
                    .build();
        }
        @GetMapping("/getUserInfo")
        ApiResponse<UserResponse> getUserInfo () {
            return ApiResponse.<UserResponse>builder()
                    .code(1000)
                    .message("Success")
                    .result(userService.getMyInfo())
                    .build();
        }

        @PutMapping("/{userId}")
        @PreAuthorize("hasRole('ADMIN') or @authz.isOwner(#userId)")
        ApiResponse<UserResponse> updateUser (@PathVariable String userId, @RequestBody UserUpdateRequest request) {
            return ApiResponse.<UserResponse>builder()
                    .code(1000)
                    .message("Success")
                    .result(userService.updateUser(userId, request))
                    .build();
        }

        @PutMapping("/{userId}/change-password")
        @PreAuthorize("hasRole('ADMIN') or @authz.isOwner(#userId)")
        ApiResponse<String> changePassword (@PathVariable String userId, @RequestBody ChangePasswordRequest request) {
            userService.changePassword(userId, request);
            return ApiResponse.<String>builder()
                    .code(1000)
                    .message("Success")
                    .result("Password changed successfully")
                    .build();
        }

        @DeleteMapping("/{userId}")
        ApiResponse<String> deleteUser (@PathVariable String userId) {
            userService.deleteUser(userId);
            return ApiResponse.<String>builder()
                    .code(1000)
                    .message("Success")
                    .result("User deleted successfully")
                    .build();
        }

    }
