package com.bindord.eureka.keycloak.domain.request;

import com.bindord.eureka.keycloak.validation.ExtendedEmailValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLogin {

    @ExtendedEmailValidator
    @Size(min = 7, max = 60)
    @NotBlank
    private String email;

    @Size(min = 8, max = 50)
    @NotBlank
    private String password;
}
