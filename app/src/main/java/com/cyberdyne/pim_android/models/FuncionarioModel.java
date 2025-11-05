package com.cyberdyne.pim_android.models; // Verifique se o nome do pacote está correto

import com.google.gson.annotations.SerializedName;

public class FuncionarioModel {

    @SerializedName("id")
    private int id;

    @SerializedName("nomeCompleto")
    private String nomeCompleto;

    @SerializedName("email")
    private String email;

    @SerializedName("cargo")
    private String cargo;

    @SerializedName("setor")
    private int setor; // 0 = RH, 1 = Admin, etc.

    @SerializedName("telefone")
    private String telefone;

    // Getters
    public int getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public int getSetor() { return setor; }
    public String getTelefone() { return telefone; }
}