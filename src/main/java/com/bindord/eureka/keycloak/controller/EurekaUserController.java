package com.bindord.eureka.keycloak.controller;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.domain.dto.UserPasswordDTO;
import com.bindord.eureka.keycloak.domain.request.EurekaUser;
import com.bindord.eureka.keycloak.service.UserService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("${service.ingress.context-path}/user")
@AllArgsConstructor
public class EurekaUserController {

    private UserService userService;

    @PostMapping("")
    public UserRepresentation add(@Valid @RequestBody EurekaUser user) throws CustomValidationException {
        return userService.save(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable UUID userId) throws CustomValidationException {
        userService.delete(userId);
    }

    @PutMapping("/updatePassword")
    public void updateUserPasswordById(@RequestBody UserPasswordDTO userPassword) throws CustomValidationException {
        userService.doChangePassword(userPassword);
    }


}
