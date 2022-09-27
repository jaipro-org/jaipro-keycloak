package com.bindord.eureka.keycloak.domain;

import com.bindord.eureka.keycloak.validation.ExtendedEmailValidator;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
public class User implements Serializable {

    private UUID id;

    @ExtendedEmailValidator
    @NotBlank
    @Size(min = 7, max = 60)
    private String email;

    @NotBlank
    @Size(min = 7, max = 40)
    private String password;

    private boolean enabled;

    private boolean eliminated;

    @Min(value = 1)
    @Max(value = 10)
    private Integer profile;

    private String[] roles;

    public User() {
    }

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

}
