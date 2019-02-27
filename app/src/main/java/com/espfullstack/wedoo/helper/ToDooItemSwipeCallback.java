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
import com.espfullstack.wedoo.pojo.ToDooItem;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ToDooItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ToDooItemAdapter toDooItemAdapter;
    private Drawable icon;
    private final ColorDrawable background, finishedBackground;

    public ToDooItemSwipeCallback(ToDooItemAdapter toDooItemAdapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.toDooItemAdapter = toDooItemAdapter;
        icon = context.getDrawable(R.drawable.ic_trash);
        background = new ColorDrawable(Color.RED);
        finishedBackground = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            toDooItemAdapter.delete(position);
        } else if (direction == ItemTouchHelper.RIGHT) {
            toDooItemAdapter.changeStatus(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int position = viewHolder.getAdapterPosition();

        if (position == -1) {
            return;
        }

        ColorDrawable background = finishedBackground;

        Drawable icone = icon;

        int iconMargin = (itemView.getHeight() - icone.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icone.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icone.getIntrinsicHeight();

        if (dX > 0) {
            if (viewHolder.getItemViewType() == 1) {
                finishedBackground.setColor(Color.RED);
            } else if (viewHolder.getItemViewType() == 0) {
                finishedBackground.setColor(Color.GREEN);
            }
            background = finishedBackground;
            icone = null;
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());

        } else if (dX < 0) {
            background = this.background;
            icone = this.icon;
            int iconLeft = itemView.getRight() - iconMargin - icone.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            icone.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive);
    }
}
