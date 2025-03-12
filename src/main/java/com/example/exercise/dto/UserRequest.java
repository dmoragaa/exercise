package com.example.exercise.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserRequest {

    @NotEmpty(message = "El nombre es obligatorio")
    private String name;

    @NotEmpty(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotEmpty(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "La lista de teléfonos no puede ser nula")
    @NotEmpty(message = "La lista de teléfonos no puede ser vacía")
    @Valid
    private List<PhoneRequest> phones;

    public UserRequest() {
    }

    public UserRequest(String name, String email, String password, List<PhoneRequest> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneRequest> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequest> phones) {
        this.phones = phones;
    }
}
