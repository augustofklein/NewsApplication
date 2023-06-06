package br.ucs.android.newsapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ucs.android.newsapplication.databinding.ItemFavoritosBinding;
import br.ucs.android.newsapplication.model.Favorito;
import br.ucs.android.newsapplication.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavoritosRecyclerViewAdapter extends RecyclerView.Adapter<FavoritosRecyclerViewAdapter.ViewHolder> {

    private final List<Favorito> mValues;

    public FavoritosRecyclerViewAdapter(List<Favorito> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemFavoritosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTituloView.setText(mValues.get(position).getArtigo().getTitle());
        holder.mAutorView.setText(mValues.get(position).getArtigo().getAuthor());
        holder.mDataView.setText(mValues.get(position).getData().toLocaleString());
        holder.mObservacaoView.setText(mValues.get(position).getObservacao());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTituloView;
        public final TextView mAutorView;
        public final TextView mDataView;
        public final TextView mObservacaoView;
        public Favorito mItem;

        public ViewHolder(ItemFavoritosBinding binding) {
            super(binding.getRoot());
            mTituloView = binding.tvTitulo;
            mAutorView = binding.tvAutor;
            mDataView = binding.tvData;
            mObservacaoView = binding.tvObservacao;
        }

        @Override
        public String toString() {
            return super.toString() + mTituloView.getText() + " '" + mObservacaoView.getText() + "'";
        }
    }
}