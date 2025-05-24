package com.khangdjnh.identity_keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {"/auth/login", "/auth/register", "/users"};
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                        .permitAll()
                        .anyRequest()
                        .authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                                jwtAuthenticationConverter()
                        ))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }
}
