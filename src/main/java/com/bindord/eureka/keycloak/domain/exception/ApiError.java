package com.bindord.eureka.keycloak.domain.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {

    }

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex, List<ApiSubError> subErrors) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getMessage();
        this.subErrors = subErrors;
    }

    public ApiError(HttpStatus status, String message, List<ApiSubError> subErrors) {
        this.status = status;
        this.message = message;
        this.subErrors = subErrors;
    }
}
