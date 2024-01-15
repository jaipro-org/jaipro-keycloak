package com.bindord.eureka.keycloak.repository;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.configuration.KeycloakManagement;
import com.bindord.eureka.keycloak.configuration.KeycloakResolver;
import com.bindord.eureka.keycloak.domain.dto.UserPasswordDTO;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.google.common.collect.Lists;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.protocol.oidc.client.authentication.ClientIdAndSecretCredentialsProvider;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bindord.eureka.keycloak.util.Constants.JAIPRO_MAPPER_GENERAL_PROFILE;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_CLIENT_ID;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_CLIENT_SECRET;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_GRANT_TYPE;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_GRANT_TYPE_RT;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_SECRET;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_URI_REALMS;
import static com.bindord.eureka.keycloak.util.Constants.OAUTH2_URI_TOKEN_OPERATION;
import static com.bindord.eureka.keycloak.util.Constants.Profiles.BACKOFFICE;
import static com.bindord.eureka.keycloak.util.Constants.Profiles.CUSTOMER;
import static com.bindord.eureka.keycloak.util.Constants.Profiles.SPECIALIST;

@Component
@Slf4j
public class UserRepository {

    private final Configuration keycloakConfig;

    private final KeycloakResolver keycloakResolver;

    private final KeycloakManagement keycloakManagement;

    public UserRepository(Configuration keycloakConfig, KeycloakResolver keycloakResolver, KeycloakManagement keycloakManagement) {
        this.keycloakConfig = keycloakConfig;
        this.keycloakResolver = keycloakResolver;
        this.keycloakManagement = keycloakManagement;
    }

    @Value("${keycloak.realm}")
    private String keycloakRealm;

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
        Integer profile = eurekaUser.getProfileType();
        if (profile == null || profile > 3) {
            throw new CustomValidationException("Profile type is required or is invalid");
        }
        RealmResource realmResource = keycloakResolver.resolve(eurekaUser.getProfileType()).realm(keycloakRealm);
        var clientId = this.getClientIdByProfile(profile);
        var clientDefaultRole = this.getGenericRoleByProfile(profile);
        UserRepresentation userRepresentation = new UserRepresentation();
        BeanUtils.copyProperties(eurekaUser, userRepresentation);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(eurekaUser.getPassword());
        credential.setTemporary(false);

        userRepresentation.setCredentials(List.of(credential));
        userRepresentation.setEnabled(true);

        // Get the client resource
        ClientResource clientResource = realmResource.clients().get(clientId);
        RoleResource customerRoleResource = clientResource.roles().get(clientDefaultRole);
        RoleRepresentation customerRoleRepresentation = customerRoleResource.toRepresentation();

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(JAIPRO_MAPPER_GENERAL_PROFILE, List.of(profile.toString()));
        userRepresentation.setAttributes(attributes);

        Response response = realmResource.users().create(userRepresentation);
        var resCode = response.getStatus();
        if (resCode == HttpStatus.OK.value() || resCode == HttpStatus.CREATED.value()) {
            var location = response.getLocation().toString();
            var userId = Lists.reverse(Arrays.asList(location.split("/"))).get(0);
            userRepresentation.setId(userId);

            // Get the user resource
            UserResource userResource = realmResource.users().get(userId);
            userResource.roles().clientLevel(clientId).add(List.of(customerRoleRepresentation));

            return userRepresentation;
        }
        var errDetail = response.readEntity(ErrorRepresentation.class);
        throw new CustomValidationException(errDetail.getErrorMessage());
    }

    public UserToken refreshToken(String refreshToken) {
        AuthzClient authzClient = AuthzClient.create(keycloakConfig);

        String url = keycloakConfig.getAuthServerUrl() +
                OAUTH2_URI_REALMS +
                keycloakConfig.getRealm() +
                OAUTH2_URI_TOKEN_OPERATION;

        String clientId = keycloakConfig.getResource();
        String secret = (String) keycloakConfig.getCredentials().get(OAUTH2_SECRET);
        var clientCredProv = new ClientIdAndSecretCredentialsProvider();
        var adapterConfig = new AdapterConfig();
        clientCredProv.setClientCredentials(adapterConfig, Map.of(), Map.of());
        Http http = new Http(authzClient.getConfiguration(), clientCredProv);

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
        RealmResource realmResource = keycloakResolver.buildDefault().realm(keycloakRealm);
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
        RealmResource realmResource = keycloakResolver.buildDefault().realm(keycloakRealm);
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
        RealmResource realmResource = keycloakResolver.buildDefault().realm(keycloakRealm);

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

    public String getClientIdByProfile(Integer profileType) {
        if (profileType == CUSTOMER.get()) {
            return keycloakManagement.getCredentials().get(CUSTOMER.name().toLowerCase()).getId();
        }
        if (profileType == SPECIALIST.get()) {
            return keycloakManagement.getCredentials().get(SPECIALIST.name().toLowerCase()).getId();
        }
        if (profileType == BACKOFFICE.get()) {
            return keycloakManagement.getCredentials().get(BACKOFFICE.name().toLowerCase()).getId();
        }
        return StringUtils.EMPTY;
    }

    public String getGenericRoleByProfile(Integer profileType) {
        if (profileType == CUSTOMER.get()) {
            return CUSTOMER.name().toLowerCase();
        }
        if (profileType == SPECIALIST.get()) {
            return SPECIALIST.name().toLowerCase();
        }
        if (profileType == BACKOFFICE.get()) {
            return BACKOFFICE.name().toLowerCase();
        }
        return StringUtils.EMPTY;
    }
}
