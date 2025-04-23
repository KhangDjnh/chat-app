package com.khangdjnh.identity_keycloak.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    String username;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    String email;
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    String phone;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
}
