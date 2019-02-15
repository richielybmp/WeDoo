package com.espfullstack.wedoo.helper;

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
    public void onChanged() {
        super.onChanged();
        checkIfEmpty();
    }
    private void checkIfEmpty() {
        if (view != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            view.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }
}
