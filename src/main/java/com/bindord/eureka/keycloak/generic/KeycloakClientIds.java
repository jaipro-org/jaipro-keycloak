package com.bindord.eureka.keycloak.generic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class KeycloakClientIds {

    @Value("${keycloak.client-id}")
    private String clientId;
}
