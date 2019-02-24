package com.espfullstack.wedoo.session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.ToDooActivity;

public class FormToDoItemActivity extends AppCompatActivity {

    @BindView(R.id.btn_back_form_todo_item)
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_to_do_item);
        ButterKnife.bind(this);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btn_back_form_todo_item)
    public void voltar(){
        Intent i = new Intent(this, ToDooActivity.class);
        startActivity(i);
    }
}
