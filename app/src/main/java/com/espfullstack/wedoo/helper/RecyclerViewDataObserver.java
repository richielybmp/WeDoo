package com.espfullstack.wedoo.helper;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
    private RecyclerView recyclerView;
    private View view;

    public RecyclerViewDataObserver(RecyclerView recyclerView, View view) {
        this.recyclerView = recyclerView;
        this.view = view;
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        Log.d("ONINSERTED", "INSERTED");
        checkIfEmpty();
        super.onItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        Log.d("ONREMOVED", "REMOVED");
        checkIfEmpty();
        super.onItemRangeRemoved(positionStart, itemCount);
    }

    private void checkIfEmpty() {
        if (view != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            view.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }
}
