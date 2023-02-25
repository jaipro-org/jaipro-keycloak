package com.bindord.eureka.keycloak.repository;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdminRepository {

    private final Keycloak keycloak;

    public UserAdminRepository(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Value("${keycloak.realm}")
    private String realm;

    public List<UserRepresentation> findAll() {
        RealmResource realmResource = keycloak.realm(realm);
        var users = realmResource.users();
        return users.list();
    }
}
