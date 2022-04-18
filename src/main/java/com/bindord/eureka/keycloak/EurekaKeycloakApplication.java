package com.bindord.eureka.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EurekaKeycloakApplication implements CommandLineRunner {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.credentials.secret}")
    private String secretKey;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Override
    public void run(String... args) throws Exception {
        RealmResource realmResource = keycloak.realm(realm);
        var users = realmResource.users();
        users.list().forEach(user -> System.out.println(user.getEmail()));
        var clients = realmResource.clients();
        clients.findAll().forEach(cli -> System.out.println(cli.getId()));


        System.out.println(keycloak.tokenManager().getAccessTokenString());
        Keycloak instance = Keycloak.getInstance(
                authUrl,
                realm, "dummy", "Stalker@147",
                clientId, secretKey);
        System.out.println(instance.tokenManager().getAccessTokenString());

    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaKeycloakApplication.class, args);
    }

}
