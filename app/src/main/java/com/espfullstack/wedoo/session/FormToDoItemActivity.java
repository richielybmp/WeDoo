package com.espfullstack.wedoo.session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.ToDooActivity;
import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.pojo.ToDoo;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FormToDoItemActivity extends AppCompatActivity {

    @BindView(R.id.btn_back_form_todo_item)
    ImageButton btnBack;

    @BindView(R.id.fab_createToDooItem)
    FloatingActionButton btnCreateTodoItem;

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    ToDoo toDoo;
    ToDooItem toDooItem;
    ToDooItemController toDooItemController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_to_do_item);
        ButterKnife.bind(this);

        toDooItemController = new ToDooItemController(this);
        toDooItem = new ToDooItem();

        Bundle bundle = getIntent().getExtras();
        toDoo =  (ToDoo) bundle.getSerializable("todoo");
        Toast.makeText(this, "Id "+ toDoo.getId(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.fab_createToDooItem)
    public void cadastrar(){
        if(inputsValidate()){
            toDooItemController.addToDo(toDoo.getId(), toDooItem);
            voltar();
        }
    }

    @OnClick(R.id.btn_back_form_todo_item)
    public void voltar(){
        //Todo remover intent e adicionar event buster
        Intent i = new Intent(this, ToDooActivity.class);
        i.putExtra("todoo", toDoo);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public Boolean inputsValidate(){
        String _title = edtTitle.getText().toString();
        String _description = edtDescription.getText().toString();

        if(TextUtils.isEmpty(_title)){
            Toast.makeText(this, "The field Title is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        toDooItem.setTitle(edtTitle.getText().toString());
        toDooItem.setDescription(edtDescription.getText().toString());

        return true;
    }
}
