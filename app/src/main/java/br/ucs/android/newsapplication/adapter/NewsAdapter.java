package br.ucs.android.newsapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.model.Artigo;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MovieViewHolder> {

    private List<Artigo> artigos;
    private int rowLayout;
    private Context context;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView title;
        TextView author;
        TextView source;

        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.article_layout);
            title = (TextView) v.findViewById(R.id.tvTituloArtigo);
            author = (TextView) v.findViewById(R.id.tvAutorArtigo);
            source = (TextView) v.findViewById(R.id.tvSourceArtigo);
        }
    }

    public NewsAdapter(List<Artigo> artigos, int rowLayout, Context context) {
        this.artigos = artigos;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.title.setText(artigos.get(position).getTitle());
        holder.author.setText(artigos.get(position).getAuthor());
        holder.source.setText(artigos.get(position).getSource().getName());
    }

    @Override
    public int getItemCount() {
        return artigos.size();
    }

}