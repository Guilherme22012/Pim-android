package com.cyberdyne.pim_android.network; // Verifique se o nome do seu pacote está correto

// --- IMPORTAÇÕES ADICIONADAS ---
import com.cyberdyne.pim_android.models.ChamadoModel;
import com.cyberdyne.pim_android.models.FuncionarioModel;
import java.util.List; // Importa a classe 'List' do Java
// --- FIM DAS IMPORTAÇÕES ---

import com.cyberdyne.pim_android.models.LoginRequestDto;
import com.cyberdyne.pim_android.models.LoginResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET; // <-- ADICIONADO
import retrofit2.http.POST;

public interface ApiService {

    // Endpoint de Login (já existia)
    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequest);

    // --- ENDPOINTS ADICIONADOS ---

    /**
     * Busca a lista de chamados do usuário.
     * A API (ChamadoController) já sabe quem é o usuário (Supervisor/Analista)
     * pelo Token JWT que o nosso AuthInterceptor vai adicionar.
     */
    @GET("chamado") // Mapeia para GET /api/chamado
    Call<List<ChamadoModel>> getChamados();

    /**
     * Busca a lista de todos os funcionários.
     * (Será protegido pelo AuthInterceptor. A API vai retornar 403 Forbidden
     * se o usuário não for Supervisor, o que está correto).
     */
    @GET("funcionario") // Mapeia para GET /api/funcionario
    Call<List<FuncionarioModel>> getFuncionarios();

    // --- FIM DOS ENDPOINTS ADICIONADOS ---
}