package com.espfullstack.wedoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToDoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvToDooItem)
    RecyclerView rvToDooItem;

    @BindView(R.id.fab_todo)
    FloatingActionButton fab_todo;

    ToDooItemController toDooItemController;
    ToDooItemAdapter toDooItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        Intent i = getIntent();
        ToDoo toDoo =  (ToDoo) i.getSerializableExtra("todoos");

        toDooItemController = new ToDooItemController(this);
        toDooItemAdapter = new ToDooItemAdapter(toDooItemController.getAll(toDoo.getId()));

        rvToDooItem.setAdapter(toDooItemAdapter);
        rvToDooItem.setLayoutManager(new LinearLayoutManager(this));
        rvToDooItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Toast.makeText(this, "Titulo"+toDoo.getTitle(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @OnClick(R.id.fab_todo)
    public void onFabClick(View view) {
        Toast.makeText(this, "FAB Clicked", Toast.LENGTH_SHORT).show();
    }
    
    
}
