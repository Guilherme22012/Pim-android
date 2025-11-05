package com.cyberdyne.pim_android.adapters; // Verifique se o nome do pacote está correto

import android.content.Context;
import android.content.Intent; // <-- IMPORTAÇÃO ADICIONADA (para abrir a nova tela)
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberdyne.pim_android.ChamadoDetalheActivity; // <-- IMPORTAÇÃO ADICIONADA (a tela de detalhes)
import com.cyberdyne.pim_android.R;
import com.cyberdyne.pim_android.models.ChamadoModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChamadosAdapter extends RecyclerView.Adapter<ChamadosAdapter.ChamadoViewHolder> {

    private List<ChamadoModel> chamadosList;
    private Context context;

    // Construtor
    public ChamadosAdapter(List<ChamadoModel> chamadosList, Context context) {
        this.chamadosList = chamadosList;
        this.context = context;
    }

    // Cria o layout do item
    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(itemView);
    }

    // Preenche os dados do item
    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        // Pega o chamado atual
        ChamadoModel chamado = chamadosList.get(position);

        // Formata os dados
        String tipoStr = getTipoString(chamado.getTipo());
        String statusStr = getStatusString(chamado.getStatus());
        String dataFormatada = formatarData(chamado.getDataAbertura());

        // Coloca os dados nos TextViews
        holder.textViewChamadoId.setText("#" + chamado.getId());
        holder.textViewTipo.setText(tipoStr);
        holder.textViewDescricaoPreview.setText(chamado.getDescricao());
        holder.textViewData.setText(dataFormatada);
        holder.textViewStatus.setText(statusStr);

        // Pinta o status
        switch (chamado.getStatus()) {
            case 0: holder.textViewStatus.setTextColor(Color.RED); break;
            case 1: holder.textViewStatus.setTextColor(Color.parseColor("#FFA500")); break;
            case 2: holder.textViewStatus.setTextColor(Color.parseColor("#28a745")); break;
            default: holder.textViewStatus.setTextColor(Color.GRAY);
        }

        // --- ALTERAÇÃO: ADICIONA O CLIQUE LISTENER ---
        // Define o que acontece quando o usuário clica neste item da lista
        holder.itemView.setOnClickListener(v -> {
            // Cria a "intenção" de abrir a tela de detalhes
            Intent intent = new Intent(context, ChamadoDetalheActivity.class);

            // "Empacota" o objeto ChamadoModel inteiro (que fizemos 'Serializable')
            // e o anexa à intenção, usando a chave que definimos na outra tela
            intent.putExtra(ChamadoDetalheActivity.EXTRA_CHAMADO, chamado);

            // Inicia a nova tela (ChamadoDetalheActivity)
            context.startActivity(intent);
        });
        // --- FIM DA ALTERAÇÃO ---
    }

    @Override
    public int getItemCount() {
        return chamadosList.size();
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

    // --- ViewHolder (sem alterações) ---
    public class ChamadoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewChamadoId;
        public TextView textViewStatus;
        public TextView textViewTipo;
        public TextView textViewDescricaoPreview;
        public TextView textViewData;

        public ChamadoViewHolder(View view) {
            super(view);
            textViewChamadoId = view.findViewById(R.id.textViewChamadoId);
            textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewTipo = view.findViewById(R.id.textViewTipo);
            textViewDescricaoPreview = view.findViewById(R.id.textViewDescricaoPreview);
            textViewData = view.findViewById(R.id.textViewData);
        }
    }
}