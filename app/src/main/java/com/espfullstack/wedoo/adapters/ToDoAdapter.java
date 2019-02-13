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
import com.espfullstack.wedoo.pojo.ToDo;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ToDo> toDoList;

    public ToDoAdapter(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view;
        switch (viewType) {
            case ToDo.TAREFA:
            case ToDo.COMPRA:
            default:
                view = inflater.inflate(R.layout.todo_item, parent, false);
                return new ToDoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ToDo toDo = toDoList.get(position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case ToDo.TAREFA:
            case ToDo.COMPRA:
            default:
                ((ToDoViewHolder) holder).bind(toDo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Se quiser diferenciar as views para cada tipo de ToDo
        ToDo todo = toDoList.get(position);
        return todo.getType();
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvToDoTitle)
        TextView tvTitle;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(ToDo toDo) {
            tvTitle.setText(toDo.getTitle());
        }
    }
}
