package com.bindord.eureka.keycloak.configuration;

import com.bindord.eureka.keycloak.util.Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KeycloakClientConfig {

    public static final String SECRET_ATTRIBUTE = "secret";
    public static final String CLIENT_DEFAULT = "default";

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    private final KeycloakManagement keycloakManagement;

    public KeycloakClientConfig(KeycloakManagement keycloakManagement) {
        this.keycloakManagement = keycloakManagement;
    }

    @Bean
    public Keycloak keycloakClientCustomer() {

        var client = keycloakManagement.getCredentials().get(Constants.Profiles.CUSTOMER.name().toLowerCase());
        return KeycloakBuilder.builder()
                .grantType(Constants.OAUTH2_GRANT_TYPE_CC)
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .build();
    }

    @Bean
    public Keycloak keycloakClientSpecialist() {
        var client = keycloakManagement.getCredentials().get(Constants.Profiles.SPECIALIST.name().toLowerCase());
        return KeycloakBuilder.builder()
                .grantType(Constants.OAUTH2_GRANT_TYPE_CC)
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .build();
    }

    @Bean
    public Keycloak keycloakClientBackoffice() {
        var client = keycloakManagement.getCredentials().get(Constants.Profiles.BACKOFFICE.name().toLowerCase());
        return KeycloakBuilder.builder()
                .grantType(Constants.OAUTH2_GRANT_TYPE_CC)
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .build();
    }

    @Bean
    public Keycloak keycloakDefault() {
        var client = keycloakManagement.getCredentials().get(CLIENT_DEFAULT);
        return KeycloakBuilder.builder()
                .grantType(Constants.OAUTH2_GRANT_TYPE_CC)
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .build();
    }

    @Bean
    public org.keycloak.authorization.client.Configuration keycloakAuthzClientConfiguration() {
        var client = keycloakManagement.getCredentials().get(CLIENT_DEFAULT);
        return new org.keycloak.authorization.client.Configuration(
                keycloakAuthServerUrl,
                keycloakRealm,
                client.getClientId(),
                Map.of(SECRET_ATTRIBUTE, client.getClientSecret()),
                null
        );
    }

}
