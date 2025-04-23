package com.khangdjnh.identity_keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IdentityKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityKeycloakApplication.class, args);
	}

}
