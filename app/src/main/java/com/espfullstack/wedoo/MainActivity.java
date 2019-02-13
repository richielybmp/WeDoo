package com.espfullstack.wedoo;

import android.os.Bundle;

import com.espfullstack.wedoo.adapters.ToDoAdapter;
import com.espfullstack.wedoo.controllers.ToDoController;
import com.espfullstack.wedoo.pojo.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

    ToDoController toDoController;
    ToDoAdapter toDoAdapter;


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
        toDoController = new ToDoController(this);
        toDoAdapter = new ToDoAdapter(toDoController.getAll());

        rvToDo.setAdapter(toDoAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));
        rvToDo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvToDo.addOnScrollListener(onScrollListener);
    }



    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
