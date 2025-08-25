package com.andres.curso.springboot.app.springboot_crud.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")  
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(min = 4, max = 12)
    private String username;

    @NotBlank
    // @JsonIgnore //? nunca aparece ni entra, pero no es recomendable porque si queremos actualizar la contraseña no podríamos
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //? Solo entra (create/update), pero no aparece en las respuestas (get)
    private String password;

    @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"}) //? Evitamos el bucle infinito al serializar la respuesta. Entonces, evitamos los usuarios dentro de los roles 
    @ManyToMany
    @JoinTable(
        name = "users_rols",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    
    private List<Role> rols;

    private boolean enabled; 

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    public User() {
        rols = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRols() {
        return rols;
    }

    public void setRols(List<Role> rols) {
        this.rols = rols;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
