package com.bindord.eureka.keycloak.service;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.advice.NotFoundValidationException;
import com.bindord.eureka.keycloak.domain.User;
import com.bindord.eureka.keycloak.domain.dto.UserPasswordDTO;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.bindord.eureka.keycloak.generic.BaseService;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService<User, UUID> {

    List<UserRepresentation> getAll();

    UserToken doAuthenticate(UserLogin user);

    UserToken doRefreshToken(String refreshToken);

    User findByUsername(String username);

    UserRepresentation save(EurekaUser user) throws CustomValidationException;

    void updatePasswordById(UUID id, String nuevaPass);

    String initRecoverPass(String username) throws CustomValidationException, NotFoundValidationException;

    String doChangePassword(UserPasswordDTO userPasswordDTO) throws CustomValidationException;

    String deleteAllUsers();
}
