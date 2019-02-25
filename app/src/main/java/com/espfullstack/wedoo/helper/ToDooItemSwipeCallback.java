package com.espfullstack.wedoo.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.events.ToDooItemActionEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ToDooItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ToDooItemAdapter toDooItemAdapter;
    private Drawable icon;
    private final ColorDrawable background;

    public ToDooItemSwipeCallback(ToDooItemAdapter toDooItemAdapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.toDooItemAdapter = toDooItemAdapter;
        icon = context.getDrawable(R.drawable.ic_trash);
        background = new ColorDrawable(Color.RED);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        toDooItemAdapter.delete(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        } else {
            background.setBounds(0, 0,0,0);
        }

        background.draw(c);
        icon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
