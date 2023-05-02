package com.bindord.eureka.keycloak.controller;

import com.bindord.eureka.keycloak.domain.dto.MailDto;
import com.bindord.eureka.keycloak.service.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${service.ingress.context-path}/mail")
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@Valid @RequestBody MailDto mail) {
        emailService.sendMail(mail);
    }
}
