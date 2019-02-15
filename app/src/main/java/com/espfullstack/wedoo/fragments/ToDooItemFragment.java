package com.espfullstack.wedoo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooSavedEvent;
import com.espfullstack.wedoo.helper.RecyclerViewDataObserver;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDooItemFragment extends Fragment {

    @BindView(R.id.rvToDooItem)
    RecyclerView rvToDooItem;
    @BindView(R.id.emptyView)
    View emptyView;

    private ToDooItemController toDooItemController;
    private ToDooItemAdapter toDooItemAdapter;

    private ToDoo toDoo;

    private List<ToDooItem> toDooItems;

    public ToDooItemFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toDoo = (ToDoo) getArguments().getSerializable("todoo");

        toDooItemController = new ToDooItemController(getContext());

        if(toDoo != null) {
            toDooItems = toDooItemController.getAll(toDoo.getId());
        } else {
            toDooItems = new ArrayList<>();
        }


        toDooItemAdapter = new ToDooItemAdapter(toDooItems);
    }

    public static ToDooItemFragment getInstance(ToDoo toDoo) {
        ToDooItemFragment toDooItemFragment = new ToDooItemFragment();
        Bundle bundle =  new Bundle();
        bundle.putSerializable("todoo", toDoo);
        toDooItemFragment.setArguments(bundle);

        return toDooItemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_todoo_item, container, false);
        ButterKnife.bind(this, fragmentView);

        rvToDooItem.setAdapter(toDooItemAdapter);

        RecyclerViewDataObserver dataObserver = new RecyclerViewDataObserver(rvToDooItem, emptyView);

        toDooItemAdapter.registerAdapterDataObserver(dataObserver);
        rvToDooItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToDooItem.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return fragmentView;
    }

    @OnClick(R.id.fabToDooItem)
    public void onFabToDooItemClick(){
        if(toDoo != null) {
            //mostra o alert builder
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.no_todoo), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onToDooSavedEvent(ToDooSavedEvent event) {
        toDoo = event.getToDoo();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
