package com.bindord.eureka.keycloak.service.impl;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.advice.NotFoundValidationException;
import com.bindord.eureka.keycloak.domain.User;
import com.bindord.eureka.keycloak.domain.dto.UserPasswordDTO;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.bindord.eureka.keycloak.repository.UserAdminRepository;
import com.bindord.eureka.keycloak.repository.UserRepository;
import com.bindord.eureka.keycloak.service.UserService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    private UserAdminRepository userAdminRepository;

    @Override
    public UserToken doAuthenticate(UserLogin user) {
        return repository.authenticate(user);
    }

    @Override
    public UserToken doRefreshToken(String refreshToken) {
        return repository.refreshToken(refreshToken);
    }

    @Override
    public List<UserRepresentation> getAll() {
        return userAdminRepository.findAll();
    }

    @Override
    public UserRepresentation save(EurekaUser user) throws CustomValidationException {
        return repository.save(user);
    }

    @Override
    public String deleteAllUsers() {
        return repository.deleteAllUsers();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public void updatePasswordById(UUID id, String nuevaPass) {
    }

    @Override
    public String initRecoverPass(String username) throws CustomValidationException, NotFoundValidationException {
        return null;
    }

    @Override
    public String doChangePassword(UserPasswordDTO user) throws CustomValidationException {
        return repository.updateUserPassword(user);
    }

    @Override
    public User save(User entity) throws NotFoundValidationException, CustomValidationException {
        return null;
    }

    @Override
    public User update(User entity) throws NotFoundValidationException, CustomValidationException {
        return null;
    }

    @Override
    public User findOne(UUID id) throws NotFoundValidationException {
        return null;
    }

    @Override
    public void delete(UUID id) throws CustomValidationException {
        repository.deleteByUserId(id.toString());
    }

    @Override
    public void delete() {

    }
}
