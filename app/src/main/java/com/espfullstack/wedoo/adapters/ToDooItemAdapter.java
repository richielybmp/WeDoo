package com.espfullstack.wedoo.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.events.ToDooItemActionEvent;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.pojo.ToDooItem;

import java.util.List;
import com.espfullstack.wedoo.R;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDooItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ToDooItem> toDooItems;

    public ToDooItemAdapter(List<ToDooItem> toDooItems) {
        this.toDooItems = toDooItems;
    }

    public void add(ToDooItem toDooItem) {
        toDooItems.add(toDooItem);
        notifyItemInserted(toDooItems.size() - 1);
    }

    public void delete(int position) {
        ToDooItem item = toDooItems.remove(position);
        notifyItemRemoved(position);
        EventBus.getDefault().post(new ToDooItemActionEvent(item, ToDooItemActionEvent.ToDooItemAction.DELETED));
    }

    public void update(ToDooItem toDooItem, int position) {
        toDooItems.set(position, toDooItem);
        notifyItemChanged(position);
        EventBus.getDefault().post(new ToDooItemActionEvent(toDooItem, ToDooItemActionEvent.ToDooItemAction.UPDATED));
    }

    public ToDooItem getSelectedToDooItem(int position) {
        return toDooItems.get(position);
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
    public int getItemViewType(int position) {
        return toDooItems.get(position).getStatus();
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

    public void changeStatus(int position) {
        ToDooItem item = toDooItems.get(position);

        if (item.getStatus() == 0){
            item.setStatus(1);
        }
        else if (item.getStatus() == 1){
            item.setStatus(0);
        }
        update(item, position);
    }

    class TodooItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvToDoItemTitle)
        TextView txtTitle;
        @BindView(R.id.tvToDoItemDescription)
        TextView txtDescription;
        @BindView(R.id.rlToDooItemLayoutBackground)
        View viewBackground;

        TodooItemViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ToDooItem todooItem = toDooItems.get(position);
            EventBus.getDefault().post(new ToDooItemClickedEvent(todooItem, position));
        }

        void bind(ToDooItem toDooItem){
            txtTitle.setText(toDooItem.getTitle());
            txtDescription.setText(toDooItem.getDescription());
            viewBackground.setBackgroundColor(toDooItem.getStatus() == 0 ? Color.RED : Color.GREEN);
            // TODO: ADICIONAR A DATA EM QUE O ITEM FOI CONCLUIDO.
        }
    }
}
