package com.example.exercise.dto;


import jakarta.validation.constraints.NotEmpty;

public class PhoneRequest {

    @NotEmpty(message = "El número de teléfono es obligatorio")
    private String number;

    @NotEmpty(message = "El código de ciudad es obligatorio")
    private String citycode;

    @NotEmpty(message = "El código de país es obligatorio")
    private String countrycode;

    public PhoneRequest() {
    }

    public PhoneRequest(String number, String citycode, String countrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
