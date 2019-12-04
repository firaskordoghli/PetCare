package kordoghli.firas.petcare.Ui.MyPet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.MainActivity;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMyPetActivity extends AppCompatActivity {
    private EditText nameEt, raceEt, birthEt, colorEt, descriptionEt;
    private Spinner typerSpinner, genderSpiner;
    private ImageView petIv;
    private Button addPetBtn;

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

        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    addPet();
                }
            }
        });
    }

    private void addPet() {
        JsonObject object = new JsonObject();

        object.addProperty("name", nameEt.getText().toString().trim());
        object.addProperty("race", raceEt.getText().toString().trim());
        object.addProperty("birth_date", birthEt.getText().toString().trim());
        object.addProperty("color", colorEt.getText().toString().trim());
        object.addProperty("description", descriptionEt.getText().toString().trim());
        object.addProperty("type", typerSpinner.getSelectedItem().toString());
        object.addProperty("gender", genderSpiner.getSelectedItem().toString());
        object.addProperty("id_user", 1);

        ApiUtil.getServiceClass().addPet(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(AddMyPetActivity.this, "pet added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
