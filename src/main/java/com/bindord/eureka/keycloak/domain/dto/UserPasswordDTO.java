package com.bindord.eureka.keycloak.domain.dto;

import com.bindord.eureka.keycloak.validation.ExtendedEmailValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class UserPasswordDTO {


    @NotBlank
    @Size(min = 7, max = 40)
    private String currentPassword;

    @NotBlank
    @Size(min = 7, max = 40)
    private String nwPassword;

    private String userId;

    @ExtendedEmailValidator
    @NotBlank
    @Size(min = 7, max = 60)
    private String username;

    private String verificationCode;
}
