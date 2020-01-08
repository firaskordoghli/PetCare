package kordoghli.firas.petcare.Ui.MyPet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.JsonObject;

import java.util.Calendar;

import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramDialog extends AppCompatDialogFragment {

    private TextView dateProgramTv;
    private Spinner typeSpinner;
    private DatePickerDialog datePickerDialog;
    private Button addProgramBtn;
    private Calendar calendar;
    private Integer id_pet ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_program,null);



        dateProgramTv = view.findViewById(R.id.tvDateProgrammDialog);
        typeSpinner = view.findViewById(R.id.spinnerProgramDialog);
        addProgramBtn = view.findViewById(R.id.btnAddProgram);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.program, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        Bundle bundle = getArguments();
        id_pet = bundle.getInt("id_pet");

        builder.setView(view).setTitle("New program");

        addProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()){
                    addProgram(id_pet);
                }
            }
        });

        dateProgramTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        dateProgramTv.setText(mDay + "." + (mMonth + 1) + "." + mYear);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        return builder.create();
    }

    private void addProgram(Integer id_pet){
        JsonObject object = new JsonObject();
        object.addProperty("id_pet", id_pet);
        object.addProperty("type", typeSpinner.getSelectedItem().toString());
        object.addProperty("date", dateProgramTv.getText().toString());

        ApiUtil.getServiceClass().addProgram(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private boolean validateInputs() {
        if (dateProgramTv.getText().toString().equals("")) {
            dateProgramTv.setError("required");
            dateProgramTv.requestFocus();
            return false;
        }
        return true;
    }
}
