package com.espfullstack.wedoo.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooClickedEvent;
import com.espfullstack.wedoo.helper.ColorUtil;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.session.SessionMannager;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ToDooAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ToDoo> toDooList;
    private List<ToDoo> filteredList;

    //private Random random = new Random();
    //private float h;

    private AppCompatActivity activity;
    private String query;

    private IToDooAction action;

    public ToDooAdapter(List<ToDoo> toDooList, AppCompatActivity activity) {
        this.toDooList = toDooList;
        filteredList = new ArrayList<>(toDooList);
        //h = random.nextFloat();
        this.activity = activity;
        action = (IToDooAction) activity;
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
        final ToDoo toDoo = filteredList.get(position);
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
        ToDoo todo = filteredList.get(position);
        return todo.getType();
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void add(ToDoo todoo) {
        toDooList.add(todoo);
        filteredList.add(todoo);
        notifyItemInserted(filteredList.size() - 1);
    }

    public void update(ToDoo toDoo, int position) {
        toDooList.set(position, toDoo);
        filteredList.set(position, toDoo);

        notifyItemChanged(position);
    }

    public void edit(int position) {
        FormToDoDialog formToDoDialog = new FormToDoDialog();
        Bundle toDoData = new Bundle();
        toDoData.putSerializable("toDoData", filteredList.get(position));
        toDoData.putInt("position", position);
        formToDoDialog.setArguments(toDoData);
        formToDoDialog.show(activity.getSupportFragmentManager(), "dialog_edit_todo");
        notifyItemChanged(position);
    }

    public void delete(int position) {
        ToDoo deletedToDoo = filteredList.get(position);
        toDooList.remove(deletedToDoo);
        filteredList.remove(position);
        notifyItemRemoved(position);
        action.onToDooDeleted(deletedToDoo);
    }

    public void filter(String query) {
        this.query = query;
        filteredList.clear();
        if (!TextUtils.isEmpty(query)) {
            for (ToDoo toDoo : toDooList) {
                if (toDoo.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(toDoo);
                }
            }
        } else {
            filteredList.addAll(toDooList);
        }
        notifyDataSetChanged();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tvToDoTitle)
        TextView tvTitle;

        @BindView(R.id.tvToDoItemCount)
        TextView tvToDooItemCount;

        @BindView(R.id.cvToDoo)
        CardView cvToDoo;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            //h += ColorUtil.GOLDEN_RATIO;
            //int color = ColorUtil.generateColor(h);
            //cvToDoo.setBackgroundColor(color);
        }

        void bind(ToDoo toDoo) {
            tvTitle.setText(toDoo.getTitle(), TextView.BufferType.SPANNABLE);
            if (!TextUtils.isEmpty(query)) {
                Spannable span = (Spannable) tvTitle.getText();
                int start = span.toString().toLowerCase().indexOf(query.toLowerCase().charAt(0));
                int end = start + query.length();
                span.setSpan(new ForegroundColorSpan(0xFFFFC107), start, end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            int count = toDoo.getToDooItemList().size();
            tvToDooItemCount.setText(activity.getResources().getQuantityString(R.plurals.items, count, count));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ToDoo toDoo = toDooList.get(position);
            EventBus.getDefault().post(new ToDooClickedEvent(toDoo, position));
        }

//        @Override
//        public boolean onLongClick(View v) {
//            Toast.makeText(todoView.getContext(), "Long click detected", Toast.LENGTH_SHORT).show();
//            return true;
//        }
    }
}
