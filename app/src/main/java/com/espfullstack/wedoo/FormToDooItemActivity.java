package com.espfullstack.wedoo;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooItemActionEvent;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

public class FormToDooItemActivity extends AppCompatActivity {

    @BindView(R.id.btn_back_form_todo_item)
    ImageButton btnBack;

    @BindView(R.id.fab_createToDooItem)
    FloatingActionButton btnCreateTodoItem;

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    int toDooId;
    ToDooItem toDooItem;
    ToDooItemController toDooItemController;

    ToDooItemActionEvent.ToDooItemAction toDooItemAction;

    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_to_do_item);
        ButterKnife.bind(this);

        toDooItemController = new ToDooItemController(this);
        Bundle bundle = getIntent().getExtras();
        toDooId =  bundle.getInt("todoo");
        toDooItem = (ToDooItem) bundle.getSerializable("todoo_item");

        if (toDooItem == null) {
            toDooItem = new ToDooItem();
        } else {
            init(toDooItem);
        }
    }

    private void init(ToDooItem toDooItem) {
        isUpdate = true;
        edtTitle.setText(toDooItem.getTitle());
        edtDescription.setText(toDooItem.getDescription());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.fab_createToDooItem)
    public void cadastrar(){
        if(inputsValidate()){
            if(save()) {
                EventBus.getDefault().postSticky(new ToDooItemActionEvent(toDooItem, toDooItemAction));
                voltar();
            }
        }
    }

    private boolean save() {
        if(isUpdate) {
            return updateToDooItem();
        } else {
            return saveToDooItem();
        }
    }

    private Boolean updateToDooItem(){
        if (toDooItemController.update(toDooItem)) {
            Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.UPDATED;
            return true;
        }else {
            Toast.makeText(getApplicationContext(), "Erro ao atualizar ToDooItem", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private Boolean saveToDooItem(){
        if (toDooItemController.add(toDooId, toDooItem)) {
            Toast.makeText(getApplicationContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show();
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.SAVED;
            return true;
        }else {
            Toast.makeText(getApplicationContext(), "Erro ao atualizar ToDooItem", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @OnClick(R.id.btn_back_form_todo_item)
    public void voltar(){
        finish();
    }

    public Boolean inputsValidate(){
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            edtTitle.requestFocus();
            edtTitle.setError(getString(R.string.required));
            return false;
        }

        toDooItem.setTitle(title);
        toDooItem.setDescription(description);

        return true;
    }
}
