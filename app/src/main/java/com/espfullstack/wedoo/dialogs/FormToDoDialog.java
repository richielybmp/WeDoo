package com.espfullstack.wedoo.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.espfullstack.wedoo.Interface.ISelectedData;
import com.espfullstack.wedoo.MainActivity;
import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.pojo.ToDoo;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FormToDoDialog extends DialogFragment {

    private Spinner spinner;
    private ToDoo toDoo;
    private EditText title, description, endDate;
    private ToDooController toDooController;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_form_add_todo, null);

        toDoo = new ToDoo();
        toDooController = new ToDooController(view.getContext());

        title = view.findViewById(R.id.edtTitulo_dialog);
        description = view.findViewById(R.id.edtDescricao_dialog);
        endDate = view.findViewById(R.id.edtEncerramento_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.todo_types));

        spinner = (Spinner) view.findViewById(R.id.spTypeTodo);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selected = (TextView) view;
                //Toast.makeText(getContext(), "Você selecionou "+ selected.getText(), Toast.LENGTH_SHORT).show();
                if(!spinner.getSelectedItem().toString().equalsIgnoreCase("Selecione um tipo")){
                    switch (((TextView) view).getText().toString()){
                        case "Compras":
                            toDoo.setType(1);
                            break;
                        case "Tarefas":
                            toDoo.setType(0);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(inflater.inflate(R.layout.dialog_form_add_todo, null))
                .setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDoo.setTitle(title.getText().toString());
                        toDoo.setDescription(description.getText().toString());
                        toDoo.setEndDate(endDate.getText().toString());

                        if(toDooController.addToDoo(toDoo)) {
                            Toast.makeText(view.getContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                            mCallback.onSelectedData(true);
                        }
                        else {
                            Toast.makeText(view.getContext(), "Falha ao salvar", Toast.LENGTH_SHORT).show();
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

    private ISelectedData mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ISelectedData) context;
        }
        catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

}
