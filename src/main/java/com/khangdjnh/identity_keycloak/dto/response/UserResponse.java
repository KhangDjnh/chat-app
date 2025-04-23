package com.khangdjnh.identity_keycloak.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String password;
    String email;
    String phone;
    LocalDate dateOfBirth;
}
