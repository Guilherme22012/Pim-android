package com.cyberdyne.pim_android; // Verifique se o nome do seu pacote está correto

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent; // Importação para navegação
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Para mensagens "TODO"

import com.cyberdyne.pim_android.utils.SessionManager; // Importa nosso "cofre"

public class DashboardActivity extends AppCompatActivity {

    // 1. Declarar nossas variáveis
    private SessionManager sessionManager;
    private TextView textViewBemVindo;
    private Button buttonVerChamados;
    private Button buttonGerenciarFuncionarios;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Conecta este código ao nosso layout XML

        // 2. Inicializar o SessionManager ("cofre")
        sessionManager = new SessionManager(this);

        // 3. Conectar as variáveis aos componentes do layout XML
        textViewBemVindo = findViewById(R.id.textViewBemVindo);
        buttonVerChamados = findViewById(R.id.buttonVerChamados);
        buttonGerenciarFuncionarios = findViewById(R.id.buttonGerenciarFuncionarios);
        buttonLogout = findViewById(R.id.buttonLogout);

        // 4. Carregar os dados do usuário
        carregarDadosDoUsuario();

        // 5. Configurar os cliques dos botões
        configurarBotoes();
    }

    private void carregarDadosDoUsuario() {
        // Busca o nome e o cargo salvos no "cofre"
        String nome = sessionManager.getUserName();
        String cargo = sessionManager.getUserCargo();

        // Atualiza o texto de boas-vindas
        if (nome != null && !nome.isEmpty()) {
            textViewBemVindo.setText("Bem-vindo, " + nome + "!");
        }

        // Mostra o botão "Gerenciar Funcionários" APENAS se o cargo for "Supervisor"
        if ("Supervisor".equals(cargo)) {
            buttonGerenciarFuncionarios.setVisibility(View.VISIBLE); // Torna o botão visível
        }
    }

    private void configurarBotoes() {
        // --- Botão Ver Chamados ---
        buttonVerChamados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // --- CORREÇÃO AQUI ---
                // O nome da classe atual é 'DashboardActivity.this'
                Intent intent = new Intent(DashboardActivity.this, ChamadosActivity.class);
                startActivity(intent);
                // --- FIM DA CORREÇÃO ---
            }
        });

        // --- Botão Gerenciar Funcionários ---
        buttonGerenciarFuncionarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navegar para a tela de funcionários
                Toast.makeText(DashboardActivity.this, "TODO: Abrir tela de funcionários", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Botão Logout ---
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Limpa o "cofre" (apaga o token)
                sessionManager.clearSession();

                // 2. Cria uma "intenção" de voltar para a tela de Login (MainActivity)
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);

                // 3. Limpa o histórico de telas
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // 4. Executa a navegação
                startActivity(intent);

                // 5. Fecha a tela atual do Dashboard
                finish();
            }
        });
    }
}