package com.cyberdyne.pim_android.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable; // <-- IMPORTAÇÃO ADICIONADA

// "implements Serializable" permite que este objeto seja passado entre Activities
public class ChamadoModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("tipo")
    private int tipo;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("nivelUrgencia")
    private int nivelUrgencia;

    @SerializedName("status")
    private int status;

    @SerializedName("dataAbertura")
    private String dataAbertura;

    @SerializedName("resolucao")
    private String resolucao;

    @SerializedName("anexoUrl")
    private String anexoUrl;

    @SerializedName("resolvidoPorIA")
    private boolean resolvidoPorIA;

    @SerializedName("funcionarioId")
    private int funcionarioId;

    // Getters (para acessar os dados)
    public int getId() { return id; }
    public int getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public int getNivelUrgencia() { return nivelUrgencia; }
    public int getStatus() { return status; }
    public String getDataAbertura() { return dataAbertura; }
    public String getResolucao() { return resolucao; }
    public String getAnexoUrl() { return anexoUrl; }
    public boolean isResolvidoPorIA() { return resolvidoPorIA; }
    public int getFuncionarioId() { return funcionarioId; }
}