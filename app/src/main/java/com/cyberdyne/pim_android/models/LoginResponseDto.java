package com.cyberdyne.pim_android.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponseDto {


    @SerializedName("token")
    private String token;

    @SerializedName("cargo")
    private String cargo;

    @SerializedName("nome")
    private String nome;

    @SerializedName("id")
    private int id;


    public String getToken() {
        return token;
    }

    public String getCargo() {
        return cargo;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }


    public LoginResponseDto() {

    }
}