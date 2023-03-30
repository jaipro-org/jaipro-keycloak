package com.bindord.eureka.keycloak.generic;

import com.bindord.eureka.keycloak.advice.CustomValidationException;
import com.bindord.eureka.keycloak.advice.NotFoundValidationException;

import java.util.List;

public interface BaseService<T, V> {

    T save(T entity) throws NotFoundValidationException, CustomValidationException;

    T update(T entity) throws NotFoundValidationException, CustomValidationException;

    T findOne(V id) throws NotFoundValidationException;

    void delete(V id) throws CustomValidationException;

    void delete();

    List<T> findAll();
}
