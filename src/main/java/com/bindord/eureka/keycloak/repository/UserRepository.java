package com.bindord.eureka.keycloak.repository;

import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

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

    public UserToken authenticate(UserLogin user) {
//        RealmResource realmResource = keycloak.realm(realm);
//        var users = realmResource.users();
//        users.list().forEach(user -> System.out.println(user.getEmail()));
//        var clients = realmResource.clients();
//        clients.findAll().forEach(cli -> System.out.println(cli.getId()));

//        System.out.println(keycloak.tokenManager().getAccessTokenString());

        String username = user.getEmail();//peterpaul.0194@gmail.com
        String pwd = user.getPassword();//stalker147

//        Keycloak instance = Keycloak.getInstance(
//                authUrl,
//                realm,
//                username, pwd,
//                clientId, secretKey);
//        System.out.println(instance.tokenManager().getAccessTokenString());

        AuthzClient authzClient = AuthzClient.create();
        AccessTokenResponse response = authzClient.obtainAccessToken(username, pwd);
        var userToken = new UserToken();
        BeanUtils.copyProperties(response, userToken);
        return userToken;
    }
}
