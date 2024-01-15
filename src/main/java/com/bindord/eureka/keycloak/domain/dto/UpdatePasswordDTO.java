package com.bindord.eureka.keycloak.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class UpdatePasswordDTO {

    @NotBlank
    @Size(min = 7, max = 40)
    private String nwPassword;

    private String userId;
}
