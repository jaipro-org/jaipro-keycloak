package com.bindord.eureka.keycloak.advice;

import com.bindord.eureka.keycloak.domain.exception.ApiError;
import com.bindord.eureka.keycloak.domain.exception.ApiSubError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "La validación ha fallado";

        List<ApiSubError> errors = new ArrayList<>();
        for (FieldError x : ex.getBindingResult().getFieldErrors()) {
            ApiSubError appError = new ApiSubError(x.getObjectName(), x.getField(), x.getRejectedValue(), x.getDefaultMessage());
            LOGGER.warn(appError.toString());
            errors.add(appError);
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex, errors));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Validation has failed";
        List<ApiSubError> errors = new ArrayList<>();
        LOGGER.warn(((ServletWebRequest) request).getRequest().getRequestURI());
        for (FieldError x : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ApiSubError(x.getObjectName(), x.getField(), x.getRejectedValue(), x.getDefaultMessage()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, errors));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Handle missing servlet request parameter";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
}
