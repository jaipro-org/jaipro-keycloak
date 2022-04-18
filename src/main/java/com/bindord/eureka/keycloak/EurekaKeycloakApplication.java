package com.bindord.eureka.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
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
//        RealmResource realmResource = keycloak.realm(realm);
//        var users = realmResource.users();
//        users.list().forEach(user -> System.out.println(user.getEmail()));
//        var clients = realmResource.clients();
//        clients.findAll().forEach(cli -> System.out.println(cli.getId()));

//        System.out.println(keycloak.tokenManager().getAccessTokenString());
        String username = "peterpaul.0194@gmail.com";
        String pwd = "stalker147";

        Keycloak instance = Keycloak.getInstance(
                authUrl,
                realm, username, pwd,
                clientId, secretKey);
        System.out.println("User authentication in process: user --> " + username);
        System.out.println(instance.tokenManager().getAccessTokenString());
        AuthzClient authzClient = AuthzClient.create();
        Thread.sleep(2000);
        AccessTokenResponse response = authzClient.obtainAccessToken(username, pwd);
        System.out.println(response.getToken());
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaKeycloakApplication.class, args);
    }

}
