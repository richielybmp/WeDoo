package com.espfullstack.wedoo.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.pojo.ToDoo;

import java.util.List;

public class ToDooAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ToDoo> toDooList;

    public ToDooAdapter(List<ToDoo> toDooList) {
        this.toDooList = toDooList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view;
        switch (viewType) {
            case ToDoo.TAREFA:
            case ToDoo.COMPRA:
            default:
                view = inflater.inflate(R.layout.todo_item, parent, false);
                return new ToDoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ToDoo toDoo = toDooList.get(position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case ToDoo.TAREFA:
            case ToDoo.COMPRA:
            default:
                ((ToDoViewHolder) holder).bind(toDoo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Se quiser diferenciar as views para cada tipo de ToDoo
        ToDoo todo = toDooList.get(position);
        return todo.getType();
    }

    @Override
    public int getItemCount() {
        return toDooList.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvToDoTitle)
        TextView tvTitle;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(ToDoo toDoo) {
            tvTitle.setText(toDoo.getTitle());
        }
    }
}
