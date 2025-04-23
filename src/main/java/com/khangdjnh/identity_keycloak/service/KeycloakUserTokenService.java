package com.khangdjnh.identity_keycloak.service;

import com.khangdjnh.identity_keycloak.dto.request.LoginRequest;
import com.khangdjnh.identity_keycloak.entity.User;
import com.khangdjnh.identity_keycloak.exception.AppException;
import com.khangdjnh.identity_keycloak.exception.ErrorCode;
import com.khangdjnh.identity_keycloak.identity.UserTokenExchangeParam;
import com.khangdjnh.identity_keycloak.identity.UserTokenExchangeResponse;
import com.khangdjnh.identity_keycloak.repository.IdentityClient;
import com.khangdjnh.identity_keycloak.repository.UserRepository;
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



    public synchronized String getAccessToken(LoginRequest request) {
        if(cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
            refreshToken(request);
        }
        return cachedToken;
    }
    private void refreshToken(LoginRequest request) {
        UserTokenExchangeParam param = UserTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(clientId)
                .client_secret(clientSecret)
                .username(getKeycloakUsername(request.getEmail()))
                .password(request.getPassword())
                .scope("openid")
                .build();

        UserTokenExchangeResponse response = identityClient.exchangeUserToken(param);

        this.cachedToken = response.getAccessToken();
        this.tokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn()));
    }
    private String getKeycloakUsername (String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getUsername();
    }

}
