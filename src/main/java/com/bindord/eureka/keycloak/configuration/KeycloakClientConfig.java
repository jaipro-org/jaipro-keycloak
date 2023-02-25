package com.bindord.eureka.keycloak.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KeycloakClientConfig {

    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String SECRET_ATTRIBUTE = "secret";

    @Value("${keycloak.credentials.secret}")
    private String secretKey;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;
    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(authUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(secretKey)
                .build();
    }

    @Bean
    public org.keycloak.authorization.client.Configuration keycloakAuthzClientConfiguration() {
        return new org.keycloak.authorization.client.Configuration(
                authUrl,
                realm,
                clientId,
                Map.of(SECRET_ATTRIBUTE, secretKey),
                null
        );
    }

}
