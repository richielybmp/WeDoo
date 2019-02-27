package com.espfullstack.wedoo.helper;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.pojo.ToDooItem;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            //List<ToDooItem> toDooItems = toDooAdapter.getSelectedToDoo(position).getToDooItemList();
            //boolean isAllDone = toDooItems.stream().filter(o -> o.getStatus() == 0).findFirst().isPresent();
            acaoSwipeDeletar(viewHolder);
            //toDooAdapter.delete(position);
        } else if(direction == ItemTouchHelper.RIGHT){
            toDooAdapter.edit(position);
        }
    }

    private void acaoSwipeDeletar(@NonNull RecyclerView.ViewHolder viewHolder) {
        Context context = viewHolder.itemView.getContext();
        AlertDialog adDelete = new AlertDialog.Builder(context).create();
        adDelete.setTitle(context.getResources().getString(R.string.dialog_delete_Title));
        adDelete.setMessage(context.getResources().getString(R.string.dialog_delete_really_one));
        //adDelete.setMessage(context.getResources().getString(R.string.dialog_delete_really));

        final int adapterPosition = viewHolder.getAdapterPosition();

        adDelete.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                toDooAdapter.delete(adapterPosition);
            }
        });

        adDelete.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //toDooAdapter.notifyItemChanged(adapterPosition);
            }
        });

        adDelete.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //toDooAdapter.notifyItemChanged(adapterPosition);
            }
        });
        adDelete.show();
        toDooAdapter.notifyItemChanged(adapterPosition);
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
