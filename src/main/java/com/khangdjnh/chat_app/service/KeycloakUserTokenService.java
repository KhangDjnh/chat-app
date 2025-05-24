package com.khangdjnh.chat_app.service;

import com.khangdjnh.chat_app.dto.request.LoginRequest;
import com.khangdjnh.chat_app.entity.User;
import com.khangdjnh.chat_app.exception.AppException;
import com.khangdjnh.chat_app.exception.ErrorCode;
import com.khangdjnh.chat_app.identity.UserAccessTokenExchangeParam;
import com.khangdjnh.chat_app.identity.UserRefreshTokenExchangeParam;
import com.khangdjnh.chat_app.identity.UserTokenExchangeResponse;
import com.khangdjnh.chat_app.repository.IdentityClient;
import com.khangdjnh.chat_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class KeycloakUserTokenService {
    private final IdentityClient identityClient;
    private final UserRepository userRepository;

    @Value("${idp.client-id}")
    private String clientId;

    @Value("${idp.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant tokenExpiry;
    private String refreshToken;
    private Instant refreshTokenExpiry;


    public synchronized String getAccessToken(LoginRequest request) {
        if (cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
            refreshToken(request);
        }
        return cachedToken;
    }

    private void refreshToken(LoginRequest request) {
        if (refreshToken != null && Instant.now().isBefore(refreshTokenExpiry)) {
            // Gọi grant_type=refresh_token
            UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                    .grant_type("refresh_token")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .refresh_token(refreshToken)
                    .build();

            UserTokenExchangeResponse response = identityClient.exchangeUserRefreshToken(param);
            this.cachedToken = response.getAccessToken();
            this.tokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn()));
            this.refreshToken = response.getRefreshToken();
            this.refreshTokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()));
        } else {
            UserAccessTokenExchangeParam param = UserAccessTokenExchangeParam.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .username(getKeycloakUsername(request.getEmail()))
                    .password(request.getPassword())
                    .scope("openid")
                    .build();

            UserTokenExchangeResponse response = identityClient.exchangeUserAccessToken(param);

            this.cachedToken = response.getAccessToken();
            this.tokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn()));
        }
    }

    private String getKeycloakUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getUsername();
    }

}
