package com.cyberdyne.pim_android; // Verifique se o seu nome de pacote está correto

// --- Importações Necessárias ---
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent; // <-- IMPORTAÇÃO PARA NAVEGAR DE TELA
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyberdyne.pim_android.models.LoginRequestDto;
import com.cyberdyne.pim_android.models.LoginResponseDto;
import com.cyberdyne.pim_android.network.ApiClient;
import com.cyberdyne.pim_android.network.ApiService;
import com.cyberdyne.pim_android.utils.SessionManager; // <-- IMPORTA O NOVO SESSION MANAGER
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // (Constantes SharedPreferences - usadas apenas para o "Lembrar-me")
    public static final String PREFS_NAME = "PimAndroidPrefs";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_SENHA = "senha";
    public static final String PREF_LEMBRAR = "lembrar";

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextSenha;
    private CheckBox checkBoxLembrar;
    private Button buttonEntrar;
    private ProgressBar progressBarLogin;

    private ApiService apiService;
    private SharedPreferences sharedPreferences; // Para o "Lembrar-me"

    // --- ALTERAÇÃO ---
    private SessionManager sessionManager; // Variável para o "cofre"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar componentes de UI
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        checkBoxLembrar = findViewById(R.id.checkBoxLembrar);
        buttonEntrar = findViewById(R.id.buttonEntrar);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        // --- ALTERAÇÃO ---
        // Inicializa o SessionManager (o "cofre")
        sessionManager = new SessionManager(this);
        // Inicializa o ApiService (passando o 'this' como Contexto)
        apiService = ApiClient.getApiService(this);
        // --- FIM DA ALTERAÇÃO ---

        // Inicializa o SharedPreferences para o "Lembrar-me"
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Verifica se o usuário já está logado (ex: abriu o app de novo)
        // Se sim, pula direto para o Dashboard
        if (sessionManager.isLoggedIn()) {
            navegarParaDashboard();
        }

        carregarPreferencias(); // Carrega o "Lembrar-me"

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentarLogin();
            }
        });
    }

    private void carregarPreferencias() {
        boolean lembrar = sharedPreferences.getBoolean(PREF_LEMBRAR, false);
        if (lembrar) {
            editTextEmail.setText(sharedPreferences.getString(PREF_EMAIL, ""));
            editTextSenha.setText(sharedPreferences.getString(PREF_SENHA, ""));
            checkBoxLembrar.setChecked(true);
        }
    }

    private void tentarLogin() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString(); // Correção (sem .trim())

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, preencha o email e a senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCarregamento(true);
        LoginRequestDto loginRequest = new LoginRequestDto(email, senha);
        Log.d("LOGIN_REQUEST_JSON", "Enviando JSON: " + new Gson().toJson(loginRequest));

        apiService.login(loginRequest).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                mostrarCarregamento(false);

                if (response.isSuccessful() && response.body() != null) {
                    salvarPreferencias(email, senha); // Salva o "Lembrar-me"

                    // --- ALTERAÇÃO ---
                    // Login com sucesso! Pega a resposta completa
                    LoginResponseDto loginResponse = response.body();

                    // Salva os dados da sessão (Token, ID, Nome, Cargo) no "cofre"
                    sessionManager.createLoginSession(loginResponse);

                    Toast.makeText(MainActivity.this, "Bem-vindo, " + loginResponse.getNome() + "!", Toast.LENGTH_LONG).show();

                    // Navega para a próxima tela
                    navegarParaDashboard();

                    // --- FIM DA ALTERAÇÃO ---

                } else {
                    String erroMsg = "Credenciais inválidas. Tente novamente.";
                    Log.e("LoginErroAPI", "Código: " + response.code() + " | Mensagem: " + response.message());
                    Toast.makeText(MainActivity.this, erroMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                mostrarCarregamento(false);
                Log.e("LoginFalhaRede", "Erro: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Salva ou limpa as credenciais do "Lembrar-me"
     */
    private void salvarPreferencias(String email, String senha) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (checkBoxLembrar.isChecked()) {
            editor.putString(PREF_EMAIL, email);
            editor.putString(PREF_SENHA, senha);
            editor.putBoolean("lembrar", true);
        } else {
            editor.remove("email");
            editor.remove("senha");
            editor.putBoolean("lembrar", false);
        }
        editor.apply();
    }

    /**
     * Método para iniciar a DashboardActivity e fechar a MainActivity
     */
    private void navegarParaDashboard() {
        // Cria uma "intenção" de ir da tela atual (MainActivity) para a DashboardActivity
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent); // Inicia a nova tela
        finish(); // Fecha a tela de login (para o usuário não poder "voltar" para ela)
    }

    private void mostrarCarregamento(boolean carregando) {
        if (carregando) {
            progressBarLogin.setVisibility(View.VISIBLE);
            buttonEntrar.setEnabled(false);
            buttonEntrar.setText("Entrando...");
        } else {
            progressBarLogin.setVisibility(View.GONE);
            buttonEntrar.setEnabled(true);
            buttonEntrar.setText("Entrar");
        }
    }
}