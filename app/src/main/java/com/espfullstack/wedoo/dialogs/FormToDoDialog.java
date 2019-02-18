package com.espfullstack.wedoo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.pojo.ToDoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FormToDoDialog extends DialogFragment {

    private Spinner spinner;
    private ToDoo toDoo;
    private EditText title, description, endDate;
    private ToDooController toDooController;
    private int position;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_form_add_todo, null);

        toDoo = new ToDoo();
        toDooController = new ToDooController(view.getContext());

        title = view.findViewById(R.id.edtTitulo_dialog);
        description = view.findViewById(R.id.edtDescricao_dialog);
        spinner = (Spinner) view.findViewById(R.id.spTypeTodo);
        endDate = view.findViewById(R.id.edtEncerramento_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ArrayAdapter adapter = new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.todo_types));


        //Carregando strings no spinner e pegando valor caso selecionado
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Pega a string selecionada no spinner e adiciona no objeto ToDoo
                if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select a type")) {
                    toDoo.setStringType(((TextView) view).getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //pega dados caso o Dialog seja aberto para edição
        Bundle bundle = getArguments();

        if (bundle != null) {
            toDoo = (ToDoo) bundle.getSerializable("toDoData");
            position = bundle.getInt("position");
            title.setText(toDoo.getTitle());
            description.setText(toDoo.getDescription());
            endDate.setText(toDoo.getEndDate());
            endDate.setText(toDoo.getEndDate());
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(toDoo.getConvertedType()));

        }

        builder.setView(inflater.inflate(R.layout.dialog_form_add_todo, null))
                .setPositiveButton(bundle != null ? "Atualizar" : "Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDoo.setTitle(title.getText().toString());
                        toDoo.setDescription(description.getText().toString());
                        toDoo.setEndDate(endDate.getText().toString());
                        toDoo.setType(toDoo.convertTypeToInt(spinner.getSelectedItem().toString()));

                        if (bundle != null) {
                            updateToDo(view.getContext(), position);
                        } else {
                            newTodo(view.getContext());
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancelar
                    }
                });
        builder.setView(view);
        return builder.create();
    }

        private void updateToDo (Context context, int position){
            if (toDooController.updateToDoo(toDoo)) {
                Toast.makeText(context, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                mCallback.onToDooUpdated(toDoo, position);
            }else {
                Toast.makeText(context, "Erro ao atualizado ToDoo", Toast.LENGTH_SHORT).show();
            }
        }

        private void newTodo (Context context){
            if (toDooController.addToDoo(toDoo)) {
                Toast.makeText(context, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                mCallback.onToDooSaved(toDoo);
            } else {
                Toast.makeText(context, "Falha ao salvar ToDoo", Toast.LENGTH_SHORT).show();
            }
        }
        private IToDooAction mCallback;

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            try {
                mCallback = (IToDooAction) context;
            } catch (ClassCastException e) {
                Log.d("MyDialog", "Activity doesn't implement the IToDooAction interface");
            }
        }

    }
