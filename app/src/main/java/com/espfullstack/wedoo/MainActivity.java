package com.espfullstack.wedoo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.dialogs.FormToDoDialog;
import com.espfullstack.wedoo.events.ToDooClickedEvent;
import com.espfullstack.wedoo.events.ToDooHasChangedEvent;
import com.espfullstack.wedoo.helper.RecyclerViewDataObserver;
import com.espfullstack.wedoo.helper.ToDooSwipeCallback;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.session.SessionMannager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IToDooAction {

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

    private int clickedPosition = -1;

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

        toDooAdapter = new ToDooAdapter(toDoos, this);

        rvToDo.setAdapter(toDooAdapter);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));

        dataObserver = new RecyclerViewDataObserver(rvToDo, emptyView);
        toDooAdapter.registerAdapterDataObserver(dataObserver);

        rvToDo.addOnScrollListener(onScrollListener);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ToDooSwipeCallback(toDooAdapter, this));
        itemTouchHelper.attachToRecyclerView(rvToDo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tab_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        EditText edt = searchView.findViewById(R.id.search_src_text);

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                toDooAdapter.filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        super.onCreateOptionsMenu(menu);
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
    public void onToDooItemClickedEvent(ToDooClickedEvent itemClickedEvent) {
        clickedPosition = itemClickedEvent.getPosition();
        startActivityToDoo(itemClickedEvent.getToDoo());
    }

    @Subscribe(sticky = true)
    public void onToDooHasChangedEvent(ToDooHasChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        toDooAdapter.update(event.getToDoo(), clickedPosition);
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
    public void onToDooInserted(ToDoo toDoo) {
        toDooAdapter.add(toDoo);
    }

    @Override
    public void onToDooUpdated(ToDoo toDoo, int position) {
        toDooAdapter.update(toDoo, position);
    }

    @Override
    public void onToDooDeleted(ToDoo toDoo) {
        toDooController.delete(toDoo.getId());
    }
}




