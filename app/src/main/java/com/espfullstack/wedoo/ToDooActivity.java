package com.espfullstack.wedoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.espfullstack.wedoo.adapters.ToDooItemAdapter;
import com.espfullstack.wedoo.adapters.ToDooPageAdapter;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooSavedEvent;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ToDooActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ToDooPageAdapter toDooPageAdapter;
    private ToDoo toDoo;

    private int activity_result = Activity.RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        Bundle bundle = getIntent().getExtras();
        toDoo =  (ToDoo) bundle.getSerializable("todoo");

        toDooPageAdapter = new ToDooPageAdapter(getSupportFragmentManager(), this, toDoo);
        viewPager.setAdapter(toDooPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
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
    public void onToDooSavedEvent(ToDooSavedEvent event) {
        toDoo = event.getToDoo();
        activity_result = Activity.RESULT_OK;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ToDooActivity.class);
        if(activity_result == Activity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("todoo", toDoo);
            intent.putExtras(bundle);
        }
        setResult(activity_result, intent);
        finish();
    }
}
