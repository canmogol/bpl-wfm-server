package com.bplsoft.wfm.rest.dto;

public class LoginResponseDTO {

    private String result;
    private String name;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String result, String name) {
        this.result = result;
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
