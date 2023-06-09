package br.ucs.android.newsapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Favorito;
import br.ucs.android.newsapplication.model.Historico;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private BDSQLiteHelper bd;
    private List<Historico> historico;
    private int rowLayout;
    private Context context;


    public HistoricoAdapter(List<Historico> historico, int rowLayout, Context context, BDSQLiteHelper bd) {
        this.historico = historico;
        this.rowLayout = rowLayout;
        this.context = context;
        this.bd = bd;
    }

    @Override
    public HistoricoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new HistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoricoViewHolder holder, final int position) {
        holder.termo.setText(historico.get(position).getTermo());
        holder.data.setText(historico.get(position).getData().toLocaleString());
        holder.resultados.setText(historico.get(position).getQuantidade() + " artigos");
    }

    @Override
    public int getItemCount() {
        return historico.size();
    }

    class HistoricoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout historicoLayout;
        TextView termo;
        TextView data;
        TextView resultados;

        public HistoricoViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            historicoLayout = (LinearLayout) v.findViewById(R.id.historico_layout);
            termo = (TextView) v.findViewById(R.id.tvTermoHistorico);
            data = (TextView) v.findViewById(R.id.tvDataHistorico);
            resultados = (TextView) v.findViewById(R.id.tvResultHistorico);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            Historico hist = historico.get(getLayoutPosition());
            bd.deleteHistorico(hist);

            notifyDataSetChanged();

            Snackbar.make(view, R.string.favorito_removido, Snackbar.LENGTH_SHORT).show();

            return true;
        }
    }

}