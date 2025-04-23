package com.khangdjnh.identity_keycloak.repository;

import com.khangdjnh.identity_keycloak.identity.TokenExchangeParam;
import com.khangdjnh.identity_keycloak.identity.TokenExchangeResponse;
import com.khangdjnh.identity_keycloak.identity.UserCreationParam;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {
    @PostMapping(
            value = "/realms/security-keycloak/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam tokenExchangeParam);

    @PostMapping(
            value = "/admin/realms/security-keycloak/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("Authorization") String token,
            @RequestBody UserCreationParam userCreationParam);
}
