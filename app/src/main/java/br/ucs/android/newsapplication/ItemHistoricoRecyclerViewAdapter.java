package br.ucs.android.newsapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ucs.android.newsapplication.databinding.ItemHistoricoBinding;
import br.ucs.android.newsapplication.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemHistoricoRecyclerViewAdapter extends RecyclerView.Adapter<ItemHistoricoRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public ItemHistoricoRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemHistoricoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDataView.setText(mValues.get(position).id);
        holder.mTermoView.setText(mValues.get(position).content);
        holder.mResultView.setText(mValues.get(position).id);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTermoView;
        public final TextView mDataView;

        public final TextView mResultView;
        public PlaceholderItem mItem;

        public ViewHolder(ItemHistoricoBinding binding) {
            super(binding.getRoot());
            mDataView = binding.tvDataHistorico;
            mTermoView = binding.tvTermoHistorico;
            mResultView = binding.tvResultHistorico;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTermoView.getText() + "'";
        }
    }
}