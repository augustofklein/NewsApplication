package br.ucs.android.newsapplication.adapter;

import android.content.Context;
import android.content.Intent;
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
import br.ucs.android.newsapplication.activities.WebviewActivity;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.Favorito;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ArtigoAdapter extends RecyclerView.Adapter<ArtigoAdapter.ArtigoViewHolder> {

    private BDSQLiteHelper bd;
    private List<Artigo> artigos;
    private int rowLayout;
    private Context context;


    public ArtigoAdapter(List<Artigo> artigos, int rowLayout, Context context, BDSQLiteHelper bd) {
        this.artigos = artigos;
        this.rowLayout = rowLayout;
        this.context = context;
        this.bd = bd;
    }

    @Override
    public ArtigoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ArtigoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtigoViewHolder holder, final int position) {
        holder.title.setText(artigos.get(position).getTitle());
        holder.author.setText(artigos.get(position).getAuthor());
        holder.source.setText(artigos.get(position).getSource().getName());

    }

    @Override
    public int getItemCount() {
        return artigos.size();
    }

    class ArtigoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout moviesLayout;
        TextView title;
        TextView author;
        TextView source;

        public ArtigoViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            moviesLayout = (LinearLayout) v.findViewById(R.id.article_layout);
            title = (TextView) v.findViewById(R.id.tvTituloArtigo);
            author = (TextView) v.findViewById(R.id.tvAutorArtigo);
            source = (TextView) v.findViewById(R.id.tvSourceArtigo);
        }

        @Override
        public void onClick(View view) {


            Intent intent = new Intent(context, WebviewActivity.class);
            intent.putExtra("URL", artigos.get(getLayoutPosition()).getUrl());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            //Snackbar.make(view, "Você selecionou " + artigos.get(getLayoutPosition()).getTitle(), Snackbar.LENGTH_SHORT).show();
        }


        @Override
        public boolean onLongClick(View view) {

            Favorito favorito = new Favorito();
            favorito.setData(new Date());
            favorito.setObservacao("");
            favorito.setArtigo(artigos.get(getLayoutPosition()));
            favorito.setIdArtigo(0);
            bd.addFavorito(favorito);

            Snackbar.make(view, R.string.favorito_adicionado, Snackbar.LENGTH_SHORT).show();

            return true;
        }



    }


}