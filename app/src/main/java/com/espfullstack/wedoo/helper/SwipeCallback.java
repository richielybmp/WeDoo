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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ToDooAdapter toDooAdapter;

    private Drawable deleteIcon;
    private final ColorDrawable deleteBackground;

    public SwipeCallback(ToDooAdapter toDooAdapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.toDooAdapter = toDooAdapter;
        deleteIcon = context.getDrawable(R.drawable.ic_trash);
        deleteBackground = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        toDooAdapter.onMove(from, to);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        toDooAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }
        float margin = AndroidUtils.convertDpToPixel(8, recyclerView.getContext());

        deleteBackground.setBounds(itemView.getRight() + ((int) dX), itemView.getTop() + (int)margin, itemView.getRight(), itemView.getBottom());
        deleteBackground.draw(c);

        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

        int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
        int iconRight = itemView.getRight() - iconMargin;
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        deleteIcon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
