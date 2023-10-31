package com.bindord.eureka.keycloak.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
public class MailDto {


    @NotEmpty
    @Size(max = 128)
    private String subject;

    @NotEmpty
    @Size(max = 2000)
    private String content;

    private Set<String> receivers;

    public MailDto() {
    }
}

