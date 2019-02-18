package com.espfullstack.wedoo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.adapters.ToDooAdapter;
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
                if(!spinner.getSelectedItem().toString().equalsIgnoreCase("Select a type")){
                    toDoo.setStringType(((TextView) view).getText().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //pega dados caso o Dialog seja aberto para edição
        Bundle bundle = getArguments();
        if(bundle != null){
            ToDoo todo = (ToDoo) bundle.getSerializable("toDoData");
            title.setText(todo.getTitle());
            description.setText(todo.getDescription());
            endDate.setText(todo.getEndDate());
            endDate.setText(todo.getEndDate());
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(todo.getConvertedType()));

        }

        builder.setView(inflater.inflate(R.layout.dialog_form_add_todo, null))
                .setPositiveButton(bundle != null ? "Atualizar" : "Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDoo.setTitle(title.getText().toString());
                        toDoo.setDescription(description.getText().toString());
                        toDoo.setEndDate(endDate.getText().toString());
                        toDoo.setType(toDoo.convertTypeToInt(spinner.getSelectedItem().toString()));

                        if(bundle != null){
                            updateToDo(view);
                        }else{
                            newTodo(view);
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

    private void updateToDo(View view){
        if(toDooController.updateToDoo(toDoo))
            Toast.makeText(view.getContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(view.getContext(), "Erro ao atualizado ToDoo", Toast.LENGTH_SHORT).show();
    }

    private void newTodo(View view){
        if(toDooController.addToDoo(toDoo)) {
            Toast.makeText(view.getContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(view.getContext(), "Falha ao salvar ToDoo", Toast.LENGTH_SHORT).show();
        }
    }

}
