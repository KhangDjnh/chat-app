package com.khangdjnh.chat_app.identity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientTokenExchangeResponse {
    String accessToken;
    String refreshToken;
    String expiresIn;
    String refreshExpiresIn;
    String tokenType;
    String idToken;
    String scope;
}
