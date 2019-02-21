package com.espfullstack.wedoo.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.espfullstack.wedoo.Interface.IToDooAction;
import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.controllers.ToDooController;
import com.espfullstack.wedoo.pojo.ToDoo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FormToDoDialog extends DialogFragment {

    private ToDoo toDoo;
    private ToDooController toDooController;
    private int position;

    @BindView(R.id.spTypeTodo)
    Spinner spinner;

    @BindView(R.id.edtTitulo_dialog)
    EditText title;

    @BindView(R.id.edtDescricao_dialog)
    EditText description;

    @BindView(R.id.edtEncerramento_dialog)
    EditText endDate;

    @BindView(R.id.txtTitleDialog)
    TextView txtTitleDialog;

    Calendar myCalendar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_form_add_todo, null);

        ButterKnife.bind(this, view);

        toDoo = new ToDoo();
        toDooController = new ToDooController(view.getContext());

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(view);
            }
        };

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                    datePicker.show();
                }
            }
        });

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

        txtTitleDialog.setText(bundle != null ? "ToDoo" : getResources().getText(R.string.new_todoo));

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        positive.setText(bundle != null ? getResources().getText(R.string.update) : getResources().getText(R.string.add_new));

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()){
                    if (bundle != null) {
                        updateToDo(view);
                    } else {
                        newTodo(view);
                    }
                    dismiss();
                }
            }
        });

        Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negative.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        negative.setText(R.string.cancel);

        alertDialog.setView(view);
        return alertDialog;
    }

    private Boolean validateInputs() {
        String _title = title.getText().toString();
        String _description = description.getText().toString();
        String _endDate = endDate.getText().toString();
        String _type = spinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(_title)){
            Toast.makeText(getContext(), R.string.err_title_dialog, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(_description)){
            Toast.makeText(getContext(), R.string.err_description_dialog, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(_endDate)){
            Toast.makeText(getContext(), R.string.err_endDate_dialog, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(_type)){
            Toast.makeText(getContext(), R.string.err_type_dialog, Toast.LENGTH_SHORT).show();
            return false;
        }

        toDoo.setTitle(_title);
        toDoo.setDescription(_description);
        toDoo.setEndDate(_endDate);
        toDoo.setType(toDoo.convertTypeToInt(_type));

        return true;
    }

    private void updateToDo (View view){
        if (toDooController.updateToDoo(toDoo)) {
            Toast.makeText(view.getContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
            mCallback.onToDooUpdated(toDoo, position);
        }else {
            Toast.makeText(view.getContext(), "Erro ao atualizado ToDoo", Toast.LENGTH_SHORT).show();
        }
    }

    private void newTodo (View view){
        if (toDooController.addToDoo(toDoo)) {
            Toast.makeText(view.getContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show();
            mCallback.onToDooInserted(toDoo);
        } else {
            Toast.makeText(view.getContext(), "Falha ao salvar ToDoo", Toast.LENGTH_SHORT).show();
        }
    }
    private IToDooAction mCallback;

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        try {
            mCallback = (IToDooAction) context;
        } catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

    private void updateLabel(View view) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        endDate.setText(sdf.format(myCalendar.getTime()));
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
