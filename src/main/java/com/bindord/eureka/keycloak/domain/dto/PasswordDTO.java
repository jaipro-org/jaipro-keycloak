package com.bindord.eureka.keycloak.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordDTO {

    private String nwPassword;
    private String userId;
    private String username;
    private String verificationCode;

    public String getNwPassword() {
        return nwPassword;
    }

    public void setNwPassword(String nwPassword) {
        this.nwPassword = nwPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
