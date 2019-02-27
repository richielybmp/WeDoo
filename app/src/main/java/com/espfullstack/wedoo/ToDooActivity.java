package com.espfullstack.wedoo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooHasChangedEvent;
import com.espfullstack.wedoo.events.ToDooItemClickedEvent;
import com.espfullstack.wedoo.events.ToDooItemActionEvent;
import com.espfullstack.wedoo.helper.RecyclerViewDataObserver;
import com.espfullstack.wedoo.helper.ToDooItemSwipeCallback;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    RecyclerViewDataObserver dataObserver;

    private int clickedPosition = -1;
    private boolean hasChanged = false;

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
        toDooItemAdapter = new ToDooItemAdapter(toDoo.getToDooItemList());

        rvToDooItem.setAdapter(toDooItemAdapter);

        dataObserver = new RecyclerViewDataObserver(rvToDooItem, emptyView);

        toDooItemAdapter.registerAdapterDataObserver(dataObserver);
        rvToDooItem.setLayoutManager(new LinearLayoutManager(this));
        rvToDooItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ToDooItemSwipeCallback(toDooItemAdapter, this));
        itemTouchHelper.attachToRecyclerView(rvToDooItem);
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
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btn_add_toolbar:
                startToDooItemActivity(null);
            break;
        }
        return true;
    }

    public void startToDooItemActivity(@Nullable ToDooItem toDooItem) {
        Intent i = new Intent(this, FormToDooItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("todoo", toDoo.getId());
        bundle.putSerializable("todoo_item", toDooItem);
        i.putExtras(bundle);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        if(hasChanged) {
            EventBus.getDefault().postSticky(new ToDooHasChangedEvent(toDoo));
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onToDooItemClickEvent(ToDooItemClickedEvent event) {
        clickedPosition = event.getPosition();
        startToDooItemActivity(event.getToDooItem());
    }

    @Subscribe(sticky = true)
    public void onToDooItemActionEvent(ToDooItemActionEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        ToDooItem toDooItem = event.getToDooItem();
        hasChanged = true;
        switch (event.getAction()) {
            case SAVED:
                toDooItemAdapter.add(toDooItem);
                break;
            case UPDATED:
                toDooItemController.update(toDooItem);
                break;
            case DELETED:
                toDooItemController.delete(toDooItem.getId());
                break;
        }
    }
}
