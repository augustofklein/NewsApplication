package br.ucs.android.newsapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Favorito;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder> {

    private BDSQLiteHelper bd;
    private List<Favorito> favoritos;
    private int rowLayout;
    private Context context;


    public FavoritoAdapter(List<Favorito> favoritos, int rowLayout, Context context, BDSQLiteHelper bd) {
        this.favoritos = favoritos;
        this.rowLayout = rowLayout;
        this.context = context;
        this.bd = bd;
    }

    @Override
    public FavoritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritoViewHolder holder, final int position) {
        holder.title.setText(favoritos.get(position).getArtigo().getTitle());
        holder.observacao.setText(favoritos.get(position).getObservacao());
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    class FavoritoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        LinearLayout favoritosLayout;
        TextView title;
        TextView observacao;

        public FavoritoViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            favoritosLayout = (LinearLayout) v.findViewById(R.id.favorito_layout);
            title = (TextView) v.findViewById(R.id.tvFavoritoTitulo);
            observacao = (TextView) v.findViewById(R.id.tvFavoritoObservacao);
        }

        @Override
        public void onClick(View view) {
            Favorito favorito = favoritos.get(getLayoutPosition());
            favorito.setObservacao("Top: editado em " + new Date().toLocaleString());
            bd.updateFavorito(favorito);

            notifyDataSetChanged();

            Snackbar.make(view, R.string.favorito_editado, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View view) {
            Favorito favorito = favoritos.get(getLayoutPosition());
            bd.deleteFavorito(favorito);

            notifyDataSetChanged();

            Snackbar.make(view, R.string.favorito_removido, Snackbar.LENGTH_SHORT).show();

            return true;
        }
    }

}