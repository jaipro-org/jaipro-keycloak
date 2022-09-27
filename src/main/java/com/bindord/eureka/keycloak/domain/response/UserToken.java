package com.bindord.eureka.keycloak.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class UserToken {
    
    private String token;
    private long expiresIn;
    private long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private int notBeforePolicy;
    private String sessionState;
    private Map<String, Object> otherClaims = new HashMap();
    private String scope;
    private String error;
    private String errorDescription;
    private String errorUri;
}
