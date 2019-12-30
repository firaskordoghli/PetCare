package kordoghli.firas.petcare.Ui.Adoptions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;

import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdoptionActivity extends AppCompatActivity {
    private EditText nameEt,raceEt,birthEt,colorEt,descriptionEt,locationEt;
    private ImageView adoptionIv;
    private Spinner typeSpinner,genderSpinner;
    private Button addAdoptionBtn;

    private SessionManager sessionManager;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adoption);

        nameEt = findViewById(R.id.etAddAdoptionName);
        raceEt = findViewById(R.id.etAddAdoptionRace);
        birthEt = findViewById(R.id.etAddAdoptionBirth);
        colorEt = findViewById(R.id.etAddAdoptionColor);
        descriptionEt = findViewById(R.id.etAddAdoptionDescription);
        locationEt = findViewById(R.id.etAddAdoptionLocation);
        adoptionIv = findViewById(R.id.ivAddAdoptionPhoto);
        typeSpinner = findViewById(R.id.spinnerAddAdoptionType);
        genderSpinner = findViewById(R.id.spinnerAddAdoptionGender);
        addAdoptionBtn = findViewById(R.id.btnAddAdoption);

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(AddAdoptionActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

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

                datePickerDialog = new DatePickerDialog(AddAdoptionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        birthEt.setText(mDay + "." + (mMonth + 1) + "." + mYear);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        addAdoptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()){
                    addAdoption(currentUser.getId());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            locationEt.setText(selectedCarmenFeature.placeName().toString());
            try {
                JSONObject json= (JSONObject) new JSONTokener(selectedCarmenFeature.toJson()).nextValue();
                JSONArray json2 = json.getJSONArray("center");
                latitude = json2.getDouble(0);
                longitude = json2.getDouble(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAdoption(Integer id_user) {
        JsonObject object = new JsonObject();

        object.addProperty("name", nameEt.getText().toString().trim());
        object.addProperty("race", raceEt.getText().toString().trim());
        object.addProperty("birth_date", birthEt.getText().toString().trim());
        object.addProperty("color", colorEt.getText().toString().trim());
        object.addProperty("description", descriptionEt.getText().toString().trim());
        object.addProperty("type", typeSpinner.getSelectedItem().toString());
        object.addProperty("gender", genderSpinner.getSelectedItem().toString());
        object.addProperty("id_user", id_user);
        object.addProperty("latitude", latitude);
        object.addProperty("longitude", longitude);

        ApiUtil.getServiceClass().addAdoptions(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(AddAdoptionActivity.this, "adoption added", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                //startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(AddAdoptionActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
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
