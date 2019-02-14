package com.espfullstack.wedoo;

import android.content.Intent;
import android.os.Bundle;

import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tab_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                Intent signInIntent = new Intent(this.getApplicationContext(), LoginActivity.class);
                signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signInIntent);
                finish();
                Toast.makeText(this, "Log out done!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                break;
        }
        return true;
    }
}
