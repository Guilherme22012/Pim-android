package com.cyberdyne.pim_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- IMPORTAÇÃO DA TOOLBAR

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberdyne.pim_android.models.ChamadoModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChamadoDetalheActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO = "extra_chamado";

    private TextView textViewId, textViewStatus, textViewTipo, textViewUrgencia, textViewData, textViewDescricao, labelResolucao, textViewResolucao;
    private Button buttonVerAnexo;
    private ChamadoModel chamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado_detalhe);

        // --- CÓDIGO DA TOOLBAR ADICIONADO ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Habilita a seta "Voltar" (Up Button)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // --- FIM DA ADIÇÃO ---

        // Conecta as views do XML
        textViewId = findViewById(R.id.textViewDetalheId);
        textViewStatus = findViewById(R.id.textViewDetalheStatus);
        textViewTipo = findViewById(R.id.textViewDetalheTipo);
        textViewUrgencia = findViewById(R.id.textViewDetalheUrgencia);
        textViewData = findViewById(R.id.textViewDetalheData);
        textViewDescricao = findViewById(R.id.textViewDetalheDescricao);
        labelResolucao = findViewById(R.id.labelResolucao);
        textViewResolucao = findViewById(R.id.textViewDetalheResolucao);
        buttonVerAnexo = findViewById(R.id.buttonVerAnexo);

        chamado = (ChamadoModel) getIntent().getSerializableExtra(EXTRA_CHAMADO);

        if (chamado != null) {
            popularDados();
        } else {
            Toast.makeText(this, "Erro ao carregar detalhes do chamado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void popularDados() {
        String idStr = "#" + chamado.getId();
        String tipoStr = getTipoString(chamado.getTipo());
        String statusStr = getStatusString(chamado.getStatus());
        String urgenciaStr = getUrgenciaString(chamado.getNivelUrgencia());
        String dataFormatada = formatarData(chamado.getDataAbertura());

        // Define o título da tela (agora na Toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalhes do Chamado " + idStr);
        }

        textViewId.setText(idStr);
        textViewStatus.setText(statusStr);
        textViewTipo.setText(tipoStr);
        textViewUrgencia.setText(urgenciaStr);
        textViewData.setText(dataFormatada);
        textViewDescricao.setText(chamado.getDescricao());

        // Lógica de cores para o Status
        switch (chamado.getStatus()) {
            case 0:
                textViewStatus.setBackgroundResource(R.drawable.rounded_corner_red);
                break;
            case 1:
                textViewStatus.setBackgroundColor(Color.parseColor("#FFA500"));
                break;
            case 2:
                textViewStatus.setBackgroundColor(Color.parseColor("#28a745"));
                break;
        }

        // Mostra a resolução se o chamado estiver fechado
        if (chamado.getStatus() == 2 && !TextUtils.isEmpty(chamado.getResolucao())) {
            labelResolucao.setVisibility(View.VISIBLE);
            textViewResolucao.setVisibility(View.VISIBLE);
            textViewResolucao.setText(chamado.getResolucao());
        }

        // Mostra o botão de anexo se a URL existir
        if (!TextUtils.isEmpty(chamado.getAnexoUrl())) {
            buttonVerAnexo.setVisibility(View.VISIBLE);
            buttonVerAnexo.setOnClickListener(v -> {
                try {
                    // Monta a URL (assumindo que estamos no emulador)
                    String urlCompleta = "https://10.0.2.2:7086" + chamado.getAnexoUrl();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlCompleta));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Não foi possível abrir o anexo.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // --- Funções Auxiliares (sem alterações) ---
    private String formatarData(String dataString) {
        try {
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime data = LocalDateTime.parse(dataString, formatoEntrada);
            return data.format(formatoSaida);
        } catch (Exception e) {
            return dataString.split("T")[0];
        }
    }
    private String getStatusString(int status) {
        switch (status) {
            case 0: return "Aberto";
            case 1: return "Em Andamento";
            case 2: return "Fechado";
            default: return "Desconhecido";
        }
    }
    private String getTipoString(int tipo) {
        switch (tipo) {
            case 1: return "Suporte de Software";
            case 2: return "Suporte de Hardware";
            case 3: return "Problemas de Rede";
            case 4: return "Acesso";
            case 5: return "Impressora";
            case 6: return "Email";
            case 7: return "Telefone";
            case 8: return "Segurança";
            case 9: return "Solicitação";
            default: return "Desconhecido";
        }
    }
    private String getUrgenciaString(int urgencia) {
        switch (urgencia) {
            case 1: return "Pouca Urgência";
            case 2: return "Média Urgência";
            case 3: return "Muita Urgência";
            default: return "Desconhecida";
        }
    }

    // Gerencia o clique na seta "Voltar" da barra de título
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Fecha esta tela e volta para a lista
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}