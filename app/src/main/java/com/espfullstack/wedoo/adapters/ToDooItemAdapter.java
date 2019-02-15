package com.espfullstack.wedoo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.pojo.ToDooItem;

import java.util.List;
import com.espfullstack.wedoo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDooItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ToDooItem> toDooItems;


    public ToDooItemAdapter(List<ToDooItem> toDooItems) {
        this.toDooItems = toDooItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view;
        view = inflater.inflate(R.layout.todoo_item_item, parent, false);
        switch (viewType) {
            default:
                return new TodooItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ToDooItem toDooItem = toDooItems.get(position);
        int viewType = holder.getItemViewType();

        switch (viewType) {

            default:
                ((TodooItemViewHolder) holder).bind(toDooItem);
        }
    }

    @Override
    public int getItemCount() {
        return toDooItems.size();
    }


    class TodooItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvToDoItemTitle)
        TextView txtDescription;
        View todoItemView;

        TodooItemViewHolder(@NonNull View itemView){
            super(itemView);
            todoItemView = itemView;
            todoItemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "CLICKADO", Toast.LENGTH_SHORT).show();
        }

        void bind(ToDooItem toDooItem){
            txtDescription.setText(toDooItem.getDescription());
        }

    }

}
