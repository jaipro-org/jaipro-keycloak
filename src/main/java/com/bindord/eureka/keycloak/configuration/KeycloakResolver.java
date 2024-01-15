package com.bindord.eureka.keycloak.configuration;

import com.bindord.eureka.keycloak.util.Constants;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KeycloakResolver {

    private Keycloak keycloakClientCustomer;
    private Keycloak keycloakClientSpecialist;
    private Keycloak keycloakClientBackoffice;
    private Keycloak keycloakDefault;

    public Keycloak resolve(Integer profileType) {

        if (profileType == Constants.Profiles.CUSTOMER.get()) {
            return keycloakClientCustomer;
        }

        if (profileType == Constants.Profiles.SPECIALIST.get()) {
            return keycloakClientSpecialist;
        }

        if (profileType == Constants.Profiles.BACKOFFICE.get()) {
            return keycloakClientBackoffice;
        }
        return keycloakDefault;
    }

    public Keycloak buildDefault() {
        return keycloakDefault;
    }
}
