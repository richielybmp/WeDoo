package com.espfullstack.wedoo;

import android.content.Intent;
import android.os.Bundle;

import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.helper.RecyclerItemTouchHelper;
import com.espfullstack.wedoo.helper.RecyclerViewDataObserver;
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

public class MainActivity extends AppCompatActivity  implements IToDooAction {

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

    RecyclerViewDataObserver dataObserver;

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
        getSupportActionBar().setTitle(R.string.app_name);

        toDooController = new ToDooController(this);
        List<ToDoo> toDoos = toDooController.getAll();

        toDooAdapter = new ToDooAdapter(toDoos);

        rvToDo.setAdapter(toDooAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));

        dataObserver = new RecyclerViewDataObserver(rvToDo, emptyView);
        toDooAdapter.registerAdapterDataObserver(dataObserver);

        rvToDo.addOnScrollListener(onScrollListener);

        new ItemTouchHelper(new RecyclerItemTouchHelper(toDooAdapter)).attachToRecyclerView(rvToDo);
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
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onToDooItemClickedEvent(ToDooItemClickedEvent itemClickedEvent) {
        startActivityToDoo(itemClickedEvent.getToDoo());
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        FormToDoDialog formToDoDialog = new FormToDoDialog();
        formToDoDialog.show(getSupportFragmentManager(), "my_dialog");
    }

    private void startActivityToDoo(ToDoo toDoo) {
        Intent intent = new Intent(this, ToDooActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("todoo", toDoo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onToDooSaved(ToDoo toDoo) {
        toDooAdapter.addToDoo(toDoo);
    }

    @Override
    public void onToDooUpdated(ToDoo toDoo, int position) {
        toDooAdapter.updateToDoo(toDoo, position);
    }
}




