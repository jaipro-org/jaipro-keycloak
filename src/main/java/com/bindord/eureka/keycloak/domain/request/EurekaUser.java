package com.bindord.eureka.keycloak.domain.request;

import com.bindord.eureka.keycloak.validation.ExtendedEmailValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.keycloak.json.StringListMapDeserializer;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserConsentRepresentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EurekaUser {

    private String self;
    private String id;
    private String origin;
    private Long createdTimestamp;

    private Integer profileType;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;

    @ExtendedEmailValidator
    @Size(min = 7, max = 60)
    @NotBlank
    private String email;
    @Size(min = 8, max = 50)
    private String password;
    private String federationLink;
    private String serviceAccountClientId;
    @JsonDeserialize(
            using = StringListMapDeserializer.class
    )
    private Map<String, List<String>> attributes;
    private List<CredentialRepresentation> credentials;
    private Set<String> disableableCredentialTypes;
    private List<String> requiredActions;
    private List<FederatedIdentityRepresentation> federatedIdentities;
    private List<String> realmRoles;
    private Map<String, List<String>> clientRoles;
    private List<UserConsentRepresentation> clientConsents;
    private Integer notBefore;
    protected List<String> groups;
    private Map<String, Boolean> access;

    public EurekaUser() {
    }

    public String getSelf() {
        return this.self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Map<String, List<String>> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public EurekaUser singleAttribute(String name, String value) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }

        this.attributes.put(name, value == null ? new ArrayList() : Arrays.asList(value));
        return this;
    }

    public String firstAttribute(String key) {
        return this.attributes == null ? null : (this.attributes.get(key) == null ? null : (((List) this.attributes.get(key)).isEmpty() ? null : (String) ((List) this.attributes.get(key)).get(0)));
    }

    public List<CredentialRepresentation> getCredentials() {
        return this.credentials;
    }

    public void setCredentials(List<CredentialRepresentation> credentials) {
        this.credentials = credentials;
    }

    public List<String> getRequiredActions() {
        return this.requiredActions;
    }

    public void setRequiredActions(List<String> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public List<FederatedIdentityRepresentation> getFederatedIdentities() {
        return this.federatedIdentities;
    }

    public void setFederatedIdentities(List<FederatedIdentityRepresentation> federatedIdentities) {
        this.federatedIdentities = federatedIdentities;
    }

    public List<String> getRealmRoles() {
        return this.realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    public Map<String, List<String>> getClientRoles() {
        return this.clientRoles;
    }

    public void setClientRoles(Map<String, List<String>> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public List<UserConsentRepresentation> getClientConsents() {
        return this.clientConsents;
    }

    public void setClientConsents(List<UserConsentRepresentation> clientConsents) {
        this.clientConsents = clientConsents;
    }

    public Integer getNotBefore() {
        return this.notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public String getFederationLink() {
        return this.federationLink;
    }

    public void setFederationLink(String federationLink) {
        this.federationLink = federationLink;
    }

    public String getServiceAccountClientId() {
        return this.serviceAccountClientId;
    }

    public void setServiceAccountClientId(String serviceAccountClientId) {
        this.serviceAccountClientId = serviceAccountClientId;
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Set<String> getDisableableCredentialTypes() {
        return this.disableableCredentialTypes;
    }

    public void setDisableableCredentialTypes(Set<String> disableableCredentialTypes) {
        this.disableableCredentialTypes = disableableCredentialTypes;
    }

    public Map<String, Boolean> getAccess() {
        return this.access;
    }

    public void setAccess(Map<String, Boolean> access) {
        this.access = access;
    }

    public Map<String, List<String>> toAttributes() {
        Map<String, List<String>> attrs = new HashMap();
        if (this.getAttributes() != null) {
            attrs.putAll(this.getAttributes());
        }

        if (this.getUsername() != null) {
            attrs.put("username", Collections.singletonList(this.getUsername()));
        } else {
            attrs.remove("username");
        }

        if (this.getEmail() != null) {
            attrs.put("email", Collections.singletonList(this.getEmail()));
        } else {
            attrs.remove("email");
        }

        if (this.getLastName() != null) {
            attrs.put("lastName", Collections.singletonList(this.getLastName()));
        }

        if (this.getFirstName() != null) {
            attrs.put("firstName", Collections.singletonList(this.getFirstName()));
        }

        return attrs;
    }

    public Integer getProfileType() {
        return profileType;
    }

    public void setProfileType(Integer profileType) {
        this.profileType = profileType;
    }
}
