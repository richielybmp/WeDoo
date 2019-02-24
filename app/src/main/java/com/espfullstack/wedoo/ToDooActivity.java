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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.helper.RecyclerViewDataObserver;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.espfullstack.wedoo.session.FormToDoItemActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ToDooActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ToDoo toDoo;

    @BindView(R.id.rvToDooItem)
    RecyclerView rvToDooItem;
    @BindView(R.id.emptyView)
    View emptyView;

    private ToDooItemController toDooItemController;
    private ToDooItemAdapter toDooItemAdapter;

    private List<ToDooItem> toDooItems;
    RecyclerViewDataObserver dataObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        toDoo =  (ToDoo) bundle.getSerializable("todoo");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(toDoo.getTitle());

        toDooItemController = new ToDooItemController(this);

        toDooItems = toDooItemController.getAll(toDoo.getId());
        // TODO: atribuir os TodooItems na a List<ToDooItem> do ToDoo

        toDooItemAdapter = new ToDooItemAdapter(toDooItems);

        rvToDooItem.setAdapter(toDooItemAdapter);

        dataObserver = new RecyclerViewDataObserver(rvToDooItem, emptyView);

        toDooItemAdapter.registerAdapterDataObserver(dataObserver);
        rvToDooItem.setLayoutManager(new LinearLayoutManager(this));
        rvToDooItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //TODO Adicionar eventBus
            case R.id.btn_add_toolbar:
                Intent i = new Intent(this, FormToDoItemActivity.class);
                i.putExtra("todoo", toDoo);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            break;
        }

        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
