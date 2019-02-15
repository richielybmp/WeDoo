package com.espfullstack.wedoo.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.events.ToDooSavedEvent;
import com.espfullstack.wedoo.pojo.ToDoo;

import org.greenrobot.eventbus.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDooFormFragment extends Fragment {

    private ToDoo toDoo;

    @BindView(R.id.edtTitulo)
    EditText edtTitulo;
    @BindView(R.id.edtDescricao)
    EditText edtDescricao;

    private ToDooController toDooController;

    public ToDooFormFragment() {
    }

    public static ToDooFormFragment getInstance(ToDoo toDoo) {
        ToDooFormFragment toDooFormFragment = new ToDooFormFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("todoo", toDoo);
        toDooFormFragment.setArguments(bundle);
        return toDooFormFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toDoo = (ToDoo) getArguments().getSerializable("todoo");
        toDooController = new ToDooController(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_to_doo_form, container, false);
       ButterKnife.bind(this, view);


       if(toDoo != null) {
           edtTitulo.setText(toDoo.getTitle());
           edtDescricao.setText(toDoo.getDescription());
       }

       return view;
    }

    @OnClick(R.id.btnSalvar)
    public void onSalvarClick() {
        if(saveToDoo())
            EventBus.getDefault().post(new ToDooSavedEvent(toDoo));
    }

    private boolean saveToDoo() {
        if(toDoo == null) {
            toDoo = new ToDoo();
        }
        String description = edtTitulo.getText().toString();
        String titulo = edtDescricao.getText().toString();
        toDoo.setDescription(description);
        toDoo.setTitle(titulo);
        return toDooController.addToDo(toDoo);
    }

}
