package com.espfullstack.wedoo.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.helper.ColorUtil;
import com.espfullstack.wedoo.helper.RecyclerItemTouchHelper;
import com.espfullstack.wedoo.pojo.ToDoo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

public class ToDooAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerItemTouchHelper.AnimationListener {
    private List<ToDoo> toDooList;

    Random random = new Random();

    private float h;

    public ToDooAdapter(List<ToDoo> toDooList) {
        this.toDooList = toDooList;
        h = random.nextFloat();
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

    public void addToDoo(ToDoo todoo) {
        toDooList.add(todoo);
        notifyItemInserted(toDooList.size() - 1);
    }

    public void updateToDoo(ToDoo toDoo, int position) {
        toDooList.set(position, toDoo);
        notifyItemChanged(position);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        toDooList.add(toPosition, toDooList.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int direction, int position) {
        toDooList.remove(position);
        notifyItemRemoved(position);
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tvToDoTitle)
        TextView tvTitle;
        @BindView(R.id.cvToDoo)
        CardView cvToDoo;

        View todoView;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoView = itemView;
            todoView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

            todoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FormToDoDialog formToDoDialog = new FormToDoDialog();
                    Bundle toDoData = new Bundle();
                    toDoData.putSerializable("toDoData", toDooList.get(getAdapterPosition()));
                    formToDoDialog.setArguments(toDoData);
                    formToDoDialog.show(((AppCompatActivity) todoView.getContext()).getSupportFragmentManager(), "dialog_edit_todo");

                    return true;
                }
            });

        }

        void bind(ToDoo toDoo) {
            h += ColorUtil.GOLDEN_RATIO;
            int color = ColorUtil.generateColor(h);
            cvToDoo.setBackgroundColor(color);
            tvTitle.setText(toDoo.getTitle());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ToDoo toDoo = toDooList.get(position);
            EventBus.getDefault().post(new ToDooItemClickedEvent(toDoo, position));
        }

//        @Override
//        public boolean onLongClick(View v) {
//            Toast.makeText(todoView.getContext(), "Long click detected", Toast.LENGTH_SHORT).show();
//            return true;
//        }
    }
}
