package com.bindord.eureka.keycloak.advice;

import com.bindord.eureka.keycloak.domain.exception.ApiError;
import com.bindord.eureka.keycloak.domain.exception.ErrorResponse;
import com.bindord.eureka.keycloak.util.Enums;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.ws.rs.NotAuthorizedException;

import static com.bindord.eureka.keycloak.util.Enums.Error.ARCHIVO_EXCEDE_MAX_PERMITIDO;
import static com.bindord.eureka.keycloak.util.Enums.Msg.RESOURCE_NOT_FOUND;
import static com.bindord.eureka.keycloak.util.Enums.ResponseCode.EX_JACKSON_INVALID_FORMAT;
import static com.bindord.eureka.keycloak.util.Enums.ResponseCode.EX_KEYCLOAK_EXCEPTION;
import static com.bindord.eureka.keycloak.util.Enums.ResponseCode.EX_NULL_POINTER;
import static com.bindord.eureka.keycloak.util.Enums.ResponseCode.EX_NUMBER_FORMAT;
import static com.bindord.eureka.keycloak.util.Enums.ResponseCode.ILLEGAL_ARGUMENT_EXCEPTION;

@ControllerAdvice
public class ExceptionControllerAdvice {


    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_UPLOAD_FILE_SIZE;

    private static final Logger LOGGER = LogManager.getLogger(ExceptionControllerAdvice.class);

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundValidationException.class)
    public @ResponseBody
    ErrorResponse handlerNotFoundValidationException(NotFoundValidationException ex) {
        return new ErrorResponse(RESOURCE_NOT_FOUND.get(), ex.getInternalCode());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomValidationException.class)
    public @ResponseBody
    ErrorResponse handlerCustomValidationException(CustomValidationException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 2; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return new ErrorResponse(ex.getMessage(), ex.getInternalCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NumberFormatException.class)
    public @ResponseBody
    ErrorResponse handlerNumberFormatoExcception(NumberFormatException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return new ErrorResponse("Ha ocurrido un error de calculo interno", EX_NUMBER_FORMAT.get());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody
    ErrorResponse handlerIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return new ErrorResponse(ex.getMessage(), ILLEGAL_ARGUMENT_EXCEPTION.get());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public @ResponseBody
    ErrorResponse handleErrorByMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return new ErrorResponse(String.format(ARCHIVO_EXCEDE_MAX_PERMITIDO.get(), MAX_UPLOAD_FILE_SIZE), Enums.ResponseCode.EX_MAX_UPLOAD_SIZE.get());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public @ResponseBody
    ErrorResponse handleErrorByNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return new ErrorResponse("Ha ocurrido un error de apuntador nulo", EX_NULL_POINTER.get());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public @ResponseBody
    ResponseEntity<Object> handleErrorByInvalidFhandlerormatException(InvalidFormatException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, EX_JACKSON_INVALID_FORMAT.get(), ex));
    }

    @ExceptionHandler(HttpResponseException.class)
    public @ResponseBody
    ResponseEntity<Object> handleErrorByHttpResponseException(HttpResponseException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 10; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        if(ex.getStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, EX_KEYCLOAK_EXCEPTION.get(), ex));
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, EX_KEYCLOAK_EXCEPTION.get(), ex));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public @ResponseBody
    ResponseEntity<Object> handleErrorByNotAuthorizedException(NotAuthorizedException ex) {
        LOGGER.warn(ex.getMessage());
        for (int i = 0; i < 21; i++) {
            LOGGER.warn(ex.getStackTrace()[i].toString());
        }
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, EX_KEYCLOAK_EXCEPTION.get(), ex));
    }


}

