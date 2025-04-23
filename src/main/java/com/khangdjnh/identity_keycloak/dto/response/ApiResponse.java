package com.khangdjnh.identity_keycloak.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    String message;
    int code;
    T result;
}
