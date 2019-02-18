package com.espfullstack.wedoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.espfullstack.wedoo.Interface.ISelectedData;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.helper.Constant;
import com.espfullstack.wedoo.helper.RecyclerItemTouchHelper;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements ISelectedData{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvToDo)
    @Nullable
    RecyclerView rvToDo;

    @BindView(R.id.emptyList)
    TextView emptyView;

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

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        loadToDoosList();
        //rvToDo.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        rvToDo.addOnScrollListener(onScrollListener);

        new ItemTouchHelper(new RecyclerItemTouchHelper(toDooAdapter)).attachToRecyclerView(rvToDo);

    }

    private void loadToDoosList() {
        toDooController = new ToDooController(this);
        List<ToDoo> toDoos = toDooController.getAll();
        toDooAdapter = new ToDooAdapter(toDoos);
        rvToDo.setAdapter(toDooAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));

        if (toDoos.isEmpty()) {
            rvToDo.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            rvToDo.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
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
        switch (item.getItemId()) {
            case R.id.btn_profile:
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.about:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadToDoosList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onToDooItemClickedEvent(ToDooItemClickedEvent itemClickedEvent) {
        position = itemClickedEvent.getPosition();
        startActivityToDoo(itemClickedEvent.getToDoo(), Constant.UPDATE_TODOO);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        //startActivityToDoo(null, Constant.NEW_TODOO);
        FormToDoDialog formToDoDialog = new FormToDoDialog();
        formToDoDialog.show(getSupportFragmentManager(), "my_dialog");
    }

    private void startActivityToDoo(ToDoo toDoo, int requestCode) {
        Intent intent = new Intent(this, ToDooActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("todoo", toDoo);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            ToDoo todo = (ToDoo) data.getSerializableExtra("todoo");

            if(requestCode == Constant.NEW_TODOO) {
                toDooAdapter.addToDoo(todo);
            } else if(requestCode == Constant.UPDATE_TODOO) {
                toDooAdapter.updateToDoo(todo, position);
            }
            loadToDoosList();
        }
    }

    @Override
    public void onSelectedData(Boolean sucess) {
        // Use the returned value
        if (sucess)
            loadToDoosList();
    }
}




