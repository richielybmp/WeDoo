package com.espfullstack.wedoo;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.rvToDo)
    @Nullable
    RecyclerView rvToDo;

    FloatingActionButton fab;

    ToDooController toDooController;
    ToDooAdapter toDooAdapter;
    //RecyclerView.OnScrollListener onScrollListener;


    public MainFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public MainFragment(FloatingActionButton fab) {
        this.fab = fab;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    fab.show();
                    break;
                default:
                    fab.hide();
                    break;
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);


        toDooController = new ToDooController(view.getContext());
        toDooAdapter = new ToDooAdapter(toDooController.getAll());

        rvToDo.setAdapter(toDooAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvToDo.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        rvToDo.addOnScrollListener(onScrollListener);

        return view;


    }

}
