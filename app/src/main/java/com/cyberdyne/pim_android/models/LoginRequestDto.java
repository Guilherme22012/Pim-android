package com.cyberdyne.pim_android.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequestDto {

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;


    public LoginRequestDto(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // --- Getters (Opcional para DTO de requisição, mas boa prática) ---
    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    // --- Setters (Opcional) ---
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}