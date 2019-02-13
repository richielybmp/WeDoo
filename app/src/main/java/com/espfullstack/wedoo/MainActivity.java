package com.espfullstack.wedoo;

import android.content.Intent;
import android.os.Bundle;

import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvToDo)
    RecyclerView rvToDo;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ToDooController toDooController;
    ToDooAdapter toDooAdapter;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toDooController = new ToDooController(this);
        toDooAdapter = new ToDooAdapter(toDooController.getAll());

        rvToDo.setAdapter(toDooAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));
        rvToDo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvToDo.addOnScrollListener(onScrollListener);
    }



    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        startToDoActivity(new Intent(MainActivity.this, ToDoActivity.class));
    }

    private void startToDoActivity(Intent intent) {
        startActivity(intent);
    }
}
