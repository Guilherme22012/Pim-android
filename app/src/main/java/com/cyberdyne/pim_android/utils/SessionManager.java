package com.cyberdyne.pim_android.utils; // Verifique se o pacote está correto

import android.content.Context;
import android.content.SharedPreferences;
import com.cyberdyne.pim_android.models.LoginResponseDto; // Importa o DTO

public class SessionManager {

    private static final String PREFS_NAME = "PimAndroidPrefs";

    // --- Nossas chaves para o "cofre" ---
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NOME = "user_nome";
    private static final String KEY_USER_CARGO = "user_cargo";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        if (context == null) {
            throw new NullPointerException("Contexto não pode ser nulo no SessionManager");
        }
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Salva todos os dados importantes do login de uma só vez
     */
    public void createLoginSession(LoginResponseDto loginResponse) {
        editor.putString(KEY_AUTH_TOKEN, loginResponse.getToken());
        editor.putInt(KEY_USER_ID, loginResponse.getId());
        editor.putString(KEY_USER_NOME, loginResponse.getNome());
        editor.putString(KEY_USER_CARGO, loginResponse.getCargo());
        editor.apply(); // Salva as alterações
    }

    /**
     * Busca o token de autenticação salvo
     * Retorna null se não houver token
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Busca o nome do usuário salvo
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NOME, "Usuário"); // Retorna "Usuário" se não encontrar
    }

    /**
     * Busca o cargo do usuário salvo
     */
    public String getUserCargo() {
        return sharedPreferences.getString(KEY_USER_CARGO, null);
    }

    /**
     * Verifica se o usuário está logado (se existe um token)
     */
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    /**
     * Limpa todos os dados salvos (usado no Logout)
     */
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}