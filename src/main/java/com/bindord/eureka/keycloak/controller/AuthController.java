package com.bindord.eureka.keycloak.controller;

import com.bindord.eureka.keycloak.advice.NotFoundValidationException;
import com.bindord.eureka.keycloak.domain.request.RefreshToken;
import com.bindord.eureka.keycloak.domain.request.UserLogin;
import com.bindord.eureka.keycloak.domain.response.UserToken;
import com.bindord.eureka.keycloak.service.UserService;
import com.bindord.eureka.keycloak.service.email.EmailService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${service.ingress.context-path}/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    private EmailService emailService;

    @GetMapping("")
    public List<UserRepresentation> getAllUsers() throws NotFoundValidationException {
        return userService.getAll();
    }

    @PostMapping("")
    public UserToken doAuthenticate(@Valid @RequestBody UserLogin user) throws NotFoundValidationException {
        return userService.doAuthenticate(user);
    }

    @PostMapping("/refresh-token")
    public UserToken doRefreshToken(@Valid @RequestBody RefreshToken refreshToken) throws NotFoundValidationException {
        return userService.doRefreshToken(refreshToken.getRefreshToken());
    }

    @GetMapping("/temp/sendEmail")
    public void enviarCorreoInformativoConUnicoCc() {
        emailService.enviarCorreoInformativoConUnicoCc(
                "Test from backend",
                "peterpaul.182@live.com",
                "peterpaul.0194@gmail.com",
                "<h1 style=\"color: blue\">Test from backend</h1>");
    }


}
