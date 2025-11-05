package com.cyberdyne.pim_android; // Verifique se o nome do seu pacote está correto

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar; // <-- IMPORTAÇÃO ADICIONADA

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem; // <-- IMPORTAÇÃO ADICIONADA
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberdyne.pim_android.adapters.ChamadosAdapter;
import com.cyberdyne.pim_android.models.ChamadoModel;
import com.cyberdyne.pim_android.network.ApiClient;
import com.cyberdyne.pim_android.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChamados;
    private ProgressBar progressBarChamados;
    private TextView textViewInfo;

    private ApiService apiService;
    private ChamadosAdapter chamadosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        // --- CÓDIGO DA TOOLBAR ADICIONADO ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Define nossa toolbar customizada como a barra de ação

        // Habilita a seta "Voltar" (Up Button)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // --- FIM DA ADIÇÃO ---

        // Conectar as variáveis aos componentes do layout XML
        recyclerViewChamados = findViewById(R.id.recyclerViewChamados);
        progressBarChamados = findViewById(R.id.progressBarChamados);
        textViewInfo = findViewById(R.id.textViewInfo);

        // Configurar a RecyclerView
        recyclerViewChamados.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar o serviço de API
        apiService = ApiClient.getApiService(this);

        // Chamar o método para carregar os dados
        carregarChamadosDaApi();
    }

    // --- NOVO MÉTODO ADICIONADO (PARA O CLIQUE DA SETA "VOLTAR") ---
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Verifica se o item clicado é o "home" (a seta "Voltar")
        if (item.getItemId() == android.R.id.home) {
            finish(); // Fecha a tela atual (ChamadosActivity) e volta para a anterior (DashboardActivity)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- FIM DO NOVO MÉTODO ---

    /**
     * Busca os chamados na API e atualiza a lista
     */
    private void carregarChamadosDaApi() {
        progressBarChamados.setVisibility(View.VISIBLE);
        recyclerViewChamados.setVisibility(View.GONE);
        textViewInfo.setVisibility(View.GONE);

        Call<List<ChamadoModel>> call = apiService.getChamados();

        call.enqueue(new Callback<List<ChamadoModel>>() {
            @Override
            public void onResponse(Call<List<ChamadoModel>> call, Response<List<ChamadoModel>> response) {
                progressBarChamados.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<ChamadoModel> listaDeChamados = response.body();

                    if (listaDeChamados.isEmpty()) {
                        textViewInfo.setText("Nenhum chamado encontrado.");
                        textViewInfo.setVisibility(View.VISIBLE);
                    } else {
                        chamadosAdapter = new ChamadosAdapter(listaDeChamados, ChamadosActivity.this);
                        recyclerViewChamados.setAdapter(chamadosAdapter);
                        recyclerViewChamados.setVisibility(View.VISIBLE);
                    }
                } else {
                    textViewInfo.setText("Erro ao carregar chamados: " + response.message());
                    textViewInfo.setVisibility(View.VISIBLE);
                    Log.e("ChamadosActivity", "Erro na API: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ChamadoModel>> call, Throwable t) {
                progressBarChamados.setVisibility(View.GONE);
                textViewInfo.setText("Falha na conexão: " + t.getMessage());
                textViewInfo.setVisibility(View.VISIBLE);
                Log.e("ChamadosActivity", "Falha de Rede: " + t.getMessage());
            }
        });
    }
}