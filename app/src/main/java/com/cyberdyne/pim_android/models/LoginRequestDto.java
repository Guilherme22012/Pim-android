package com.cyberdyne.pim_android.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequestDto {

    // --- CORREÇÃO: Voltamos para minúsculas ---
    @SerializedName("email") // O backend espera minúsculas (camelCase)
    public String email;

    @SerializedName("senha") // O backend espera minúsculas (camelCase)
    public String senha;
    // --- FIM DA CORREÇÃO ---

    // Construtor
    public LoginRequestDto(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}