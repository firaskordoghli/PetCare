package kordoghli.firas.petcare.Ui.MyPet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;

import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.MainActivity;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMyPetActivity extends AppCompatActivity {
    private EditText nameEt, raceEt, birthEt, colorEt, descriptionEt;
    private Spinner typerSpinner, genderSpiner;
    private ImageView petIv;
    private Button addPetBtn;
    private SessionManager sessionManager;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_pet);

        nameEt = findViewById(R.id.etAddPetName);
        raceEt = findViewById(R.id.etAddPetRace);
        birthEt = findViewById(R.id.etAddPetBirth);
        colorEt = findViewById(R.id.etAddPetColor);
        descriptionEt = findViewById(R.id.etAddPetDescription);
        typerSpinner = findViewById(R.id.spinnerAddPetType);
        genderSpiner = findViewById(R.id.spinnerAddPetGender);
        petIv = findViewById(R.id.ivetAddPetPhoto);
        addPetBtn = findViewById(R.id.btnAddPet);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpiner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typerSpinner.setAdapter(typeAdapter);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        birthEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddMyPetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        birthEt.setText(mDay + "." + (mMonth + 1) + "." + mYear);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    addPet(currentUser.getId());
                }
            }
        });
    }

    private void addPet(Integer id_user) {
        JsonObject object = new JsonObject();

        object.addProperty("name", nameEt.getText().toString().trim());
        object.addProperty("race", raceEt.getText().toString().trim());
        object.addProperty("birth_date", birthEt.getText().toString().trim());
        object.addProperty("color", colorEt.getText().toString().trim());
        object.addProperty("description", descriptionEt.getText().toString().trim());
        object.addProperty("type", typerSpinner.getSelectedItem().toString());
        object.addProperty("gender", genderSpiner.getSelectedItem().toString());
        object.addProperty("id_user", id_user);

        ApiUtil.getServiceClass().addPet(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(AddMyPetActivity.this, "pet added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(AddMyPetActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        if (nameEt.getText().toString().equals("")) {
            nameEt.setError("required");
            nameEt.requestFocus();
            return false;
        }
        if (raceEt.getText().toString().equals("")) {
            raceEt.setError("required");
            raceEt.requestFocus();
            return false;
        }
        if (birthEt.getText().toString().equals("")) {
            birthEt.setError("required");
            birthEt.requestFocus();
            return false;
        }
        if (colorEt.getText().toString().equals("")) {
            colorEt.setError("required");
            colorEt.requestFocus();
            return false;
        }
        if (descriptionEt.getText().toString().equals("")) {
            descriptionEt.setError("required");
            descriptionEt.requestFocus();
            return false;
        }
        return true;
    }
}
