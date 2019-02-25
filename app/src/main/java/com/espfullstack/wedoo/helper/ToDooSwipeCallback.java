package com.espfullstack.wedoo.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ToDooSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ToDooAdapter toDooAdapter;

    private Drawable deleteIcon, editIcon;
    private final ColorDrawable deleteBackground, editBackground;

    public ToDooSwipeCallback(ToDooAdapter toDooAdapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.toDooAdapter = toDooAdapter;
        deleteIcon = context.getDrawable(R.drawable.ic_trash);
        deleteBackground = new ColorDrawable(Color.RED);
        editIcon = context.getDrawable(R.drawable.ic_edit);
        editBackground = new ColorDrawable(context.getColor(R.color.amber));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            toDooAdapter.delete(position);
        } else if(direction == ItemTouchHelper.RIGHT){
            toDooAdapter.edit(position);
        }
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        ColorDrawable background = deleteBackground;
        Drawable icon = deleteIcon;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dX > 0) {
            background = editBackground;
            icon = editIcon;
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + icon.getIntrinsicWidth();
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

        } else if (dX < 0){
            background = deleteBackground;
            icon = deleteIcon;
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
