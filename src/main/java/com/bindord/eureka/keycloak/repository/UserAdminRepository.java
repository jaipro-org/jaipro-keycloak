package com.bindord.eureka.keycloak.repository;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdminRepository {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

//    @Value("${keycloak.credentials.secret}")
//    private String secretKey;
//    @Value("${keycloak.resource}")
//    private String clientId;
//    @Value("${keycloak.auth-server-url}")
//    private String authUrl;

    public List<UserRepresentation> findAll() {
        RealmResource realmResource = keycloak.realm(realm);
        var users = realmResource.users();
        return users.list();
    }
}
