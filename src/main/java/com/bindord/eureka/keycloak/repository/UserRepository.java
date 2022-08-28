package com.bindord.eureka.keycloak.repository;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.google.common.collect.Lists;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.Response;
import java.util.Arrays;

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

    public UserRepresentation save(EurekaUser eurekaUser) throws CustomValidationException {
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRepresentation = new UserRepresentation();
        BeanUtils.copyProperties(eurekaUser, userRepresentation);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(eurekaUser.getPassword());
        credential.setTemporary(false);

        userRepresentation.setCredentials(Arrays.asList(credential));
        userRepresentation.setEnabled(true);

        Response response = realmResource.users().create(userRepresentation);
        var resCode = response.getStatus();
        if (resCode == HttpStatus.OK.value() || resCode == HttpStatus.CREATED.value()) {
            var location = response.getLocation().toString();
            var userId = Lists.reverse(Arrays.asList(location.split("/"))).get(0);
            userRepresentation.setId(userId);
            return userRepresentation;
        }
        var errDetail = response.readEntity(String.class);
        throw new CustomValidationException(errDetail);

    }
}
