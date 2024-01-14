package com.bindord.eureka.keycloak.repository;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.domain.dto.UserPasswordDTO;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.bindord.eureka.keycloak.generic.KeycloakClientIds;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_CLIENT_ID;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_CLIENT_SECRET;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_GRANT_TYPE;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_GRANT_TYPE_RT;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_SECRET;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_URI_REALMS;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_URI_TOKEN_OPERATION;

@Repository
@Slf4j
public class UserRepository {

    private final Keycloak keycloak;

    private final Configuration keycloakConfig;

    private final KeycloakClientIds keycloakClientIds;

    public UserRepository(Keycloak keycloak, Configuration keycloakConfig, KeycloakClientIds keycloakClientIds) {
        this.keycloak = keycloak;
        this.keycloakConfig = keycloakConfig;
        this.keycloakClientIds = keycloakClientIds;
    }

    @Value("${keycloak.realm}")
    private String realm;

    public UserToken authenticate(UserLogin user) {

        String username = user.getEmail();
        String pwd = user.getPassword();

        AuthzClient authzClient = AuthzClient.create(keycloakConfig);
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

        userRepresentation.setCredentials(List.of(credential));
        userRepresentation.setEnabled(true);
        // Get the client resource
        var clientOauth = realmResource.clients().get(keycloakClientIds.getClientId());
        System.out.println(clientOauth.roles());
        realmResource.clients().findAll().forEach(e-> {
            if(keycloakClientIds.getClientId().equals(e.getId())) {
                try {
                    System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(e));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(e.getId());
            System.out.println(e.getName());
        });
        ClientResource clientResource = clientOauth;
// Get the role resource for each role
        clientResource.roles().list().forEach(System.out::println);
        RoleResource customerRoleResource = clientResource.roles().get("service_full");
//        RoleResource adminRoleResource = clientResource.roles().get("adminx_jaipro_role");
        // Get the role representations
        RoleRepresentation customerRoleRepresentation = customerRoleResource.toRepresentation();
//        RoleRepresentation adminRoleRepresentation = adminRoleResource.toRepresentation();

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("profile", List.of("specialist"));
        userRepresentation.setAttributes(attributes);

        Response response = realmResource.users().create(userRepresentation);
        var resCode = response.getStatus();
        if (resCode == HttpStatus.OK.value() || resCode == HttpStatus.CREATED.value()) {
            var location = response.getLocation().toString();
            var userId = Lists.reverse(Arrays.asList(location.split("/"))).get(0);
            userRepresentation.setId(userId);


            // Get the user resource
            UserResource userResource = realmResource.users().get(userId);
            userResource.roles().clientLevel(keycloakClientIds.getClientId()).add(List.of(customerRoleRepresentation/*, adminRoleRepresentation*/));


            return userRepresentation;
        }
        var errDetail = response.readEntity(ErrorRepresentation.class);
        throw new CustomValidationException(errDetail.getErrorMessage());
    }

    public UserToken refreshToken(String refreshToken) {
        AuthzClient authzClient = AuthzClient.create(keycloakConfig);

        String url = new StringBuilder(
                keycloakConfig.getAuthServerUrl())
                .append(OAUTH2_URI_REALMS)
                .append(keycloakConfig.getRealm())
                .append(OAUTH2_URI_TOKEN_OPERATION).toString();

        String clientId = keycloakConfig.getResource();
        String secret = (String) keycloakConfig.getCredentials().get(OAUTH2_SECRET);
        Http http = new Http(authzClient.getConfiguration(), (params, headers) -> {
        });

        AccessTokenResponse accessTokenResponse = http.<AccessTokenResponse>post(url)
                .authentication()
                .client()
                .form()
                .param(OAUTH2_GRANT_TYPE, OAUTH2_GRANT_TYPE_RT)
                .param(OAUTH2_GRANT_TYPE_RT, refreshToken)
                .param(OAUTH2_CLIENT_ID, clientId)
                .param(OAUTH2_CLIENT_SECRET, secret)
                .response()
                .json(AccessTokenResponse.class)
                .execute();

        var userToken = new UserToken();
        BeanUtils.copyProperties(accessTokenResponse, userToken);
        return userToken;
    }

    public String deleteAllUsers() {
        RealmResource realmResource = keycloak.realm(realm);
        //GET TOTAL USERS
        // --> realmResource.users().count());
        //LIST USERS WHITIN RANGE (INIT, MAX)
        // --> realmResource.users().list(from, until)
        realmResource.users().list(0, realmResource.users().count()).forEach(user -> {
            realmResource.users().delete(user.getId());
            log.info("User deleted with ID {}", user.getId());
        });
        return HttpStatus.OK.toString();
    }

    public void deleteByUserId(String userId) throws CustomValidationException {
        RealmResource realmResource = keycloak.realm(realm);
        Response response = realmResource.users().delete(userId);

        var resCode = response.getStatus();
        if (resCode == HttpStatus.OK.value() || resCode == HttpStatus.NO_CONTENT.value()) {
            log.info("User deleted with ID {}", userId);
            return;
        }
        log.info("Error trying removing user with ID {}", userId);
        throw new CustomValidationException("Error trying operation");
    }

    public String updateUserPassword(UserPasswordDTO eurekaUser) {
        RealmResource realmResource = keycloak.realm(realm);

        CredentialRepresentation credential = new CredentialRepresentation();

        UserRepresentation userRepresentation = new UserRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(eurekaUser.getNwPassword());
        credential.setTemporary(false);
        userRepresentation.setCredentials(List.of(credential));
        UserResource userResource = realmResource.users().get(eurekaUser.getUserId());
        userResource.update(userRepresentation);
        return HttpStatus.OK.toString();
    }
}
