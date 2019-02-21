package com.espfullstack.wedoo.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.helper.ColorUtil;
import com.espfullstack.wedoo.helper.RecyclerItemTouchHelper;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.session.SessionMannager;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

public class ToDooAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ToDoo> toDooList;
    private Random random = new Random();
    private float h;

    private ToDoo deletedToDoo;
    private int deletedToDooPosition;

    private AppCompatActivity activity;
    private IToDooAction toDooAction;

    public ToDooAdapter(List<ToDoo> toDooList, AppCompatActivity activity) {
        this.toDooList = toDooList;
        h = random.nextFloat();
        this.activity = activity;
        toDooAction = (IToDooAction) activity;
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

    public void onMove(int fromPosition, int toPosition) {
        toDooList.add(toPosition, toDooList.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    public void deleteItem(int position) {
        deletedToDoo = toDooList.get(position);
        deletedToDooPosition = position;
        toDooList.remove(position);
        notifyItemRemoved(position);
        SessionMannager.flagForDelete(activity, deletedToDoo.getId());
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.clMainActivity);
        Snackbar snackbar = Snackbar.make(view, R.string.todoo_deleted,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete()).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if(Snackbar.Callback.DISMISS_EVENT_ACTION != event) {
                    toDooAction.onToDooDeleted(deletedToDoo);
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        SessionMannager.removeFlagged(activity);
        toDooList.add(deletedToDooPosition, deletedToDoo);
        notifyItemInserted(deletedToDooPosition);
    }

    public void editItem(int position) {
        FormToDoDialog formToDoDialog = new FormToDoDialog();
        Bundle toDoData = new Bundle();
        toDoData.putSerializable("toDoData", toDooList.get(position));
        toDoData.putInt("position", position);
        formToDoDialog.setArguments(toDoData);
        formToDoDialog.show(activity.getSupportFragmentManager(), "dialog_edit_todo");
        notifyItemChanged(position);
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tvToDoTitle)
        TextView tvTitle;

        @BindView(R.id.tvToDoItemCount)
        TextView tvToDooItemCount;

        @BindView(R.id.cvToDoo)
        CardView cvToDoo;

        View todoView;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoView = itemView;
            todoView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(ToDoo toDoo) {
            h += ColorUtil.GOLDEN_RATIO;
            int color = ColorUtil.generateColor(h);
            cvToDoo.setBackgroundColor(color);
            tvTitle.setText(toDoo.getTitle());
//            TODO: fazer o bind do textview com a quantidade de ToDoosItems
//            tvToDooItemCount.setText(toDoo.getToDooItemCount());
//            ou
//            tvToDooItemCount.setText(toDoo.todoItems.size());
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
