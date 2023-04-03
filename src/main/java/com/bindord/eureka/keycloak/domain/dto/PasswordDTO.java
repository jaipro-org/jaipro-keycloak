package com.bindord.eureka.keycloak.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class PasswordDTO {

    private String nwPassword;
    private String userId;
    private String username;
    private String verificationCode;
}
