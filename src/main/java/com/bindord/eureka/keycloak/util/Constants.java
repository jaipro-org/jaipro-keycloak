package com.bindord.eureka.keycloak.util;

public class Constants {

    public static final String OAUTH2_GRANT_TYPE = "grant_type";
    public static final String OAUTH2_CLIENT_ID = "client_id";
    public static final String OAUTH2_CLIENT_SECRET = "client_secret";
    public static final String OAUTH2_GRANT_TYPE_RT = "refresh_token";
    public static final String OAUTH2_URI_TOKEN_OPERATION = "/protocol/openid-connect/token";
    public static final String OAUTH2_URI_REALMS = "/realms/";
    public static final String OAUTH2_SECRET = "secret";
    public static final String OAUTH2_GRANT_TYPE_CC = "client_credentials";
    public static final String JAIPRO_MAPPER_GENERAL_PROFILE = "profile";

    public static final String ERROR_MESSAGE_INVALID_USER_CREDENTIALS = "La validacion de usuario y password ha fallado";
    public static final String ERROR_MESSAGE_SAME_NEW_PWD = "La nueva password no puede ser la misma que la actual";


    public enum Profiles {
        CUSTOMER(1), SPECIALIST(2), BACKOFFICE(3);

        final int id;

        Profiles(int id) {
            this.id = id;
        }

        public int get() {
            return id;
        }
    }


}
