package com.bindord.eureka.keycloak.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "keycloak.realm.jaipro.management")
public class KeycloakManagement {

    private HashMap<String, KeycloakProfile> profiles;

    private HashMap<String, CredentialProfile> credentials;

    @Setter
    @Getter
    public static class KeycloakProfile {
        private Integer id;
        private String name;
        private List<KeycloakProfileRoles> roles;
    }
    @Setter
    @Getter
    public static class KeycloakProfileRoles {
        private String name;
        private String description;

    }

    @Setter
    @Getter
    public static class CredentialProfile {
        private String id;
        private String clientId;
        private String clientSecret;
    }
}
