package kordoghli.firas.petcare.Ui.Adoptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdoptionDetailsActivity extends AppCompatActivity {
    private ImageView adoptionIv;
    private TextView adoptionNameTv,adoptionTypeTv,adoptionGenderTv,adoptionRaceTv
            ,adoptionBirthTv,adoptionColotTv,adoptionDescriptionTv;
    private Integer idAdoptionFromAdoptions = null;
    private ImageButton backBtn,deleteBtn,editBtn;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_details);

        adoptionIv = findViewById(R.id.ivAdoptionDetail);
        adoptionNameTv = findViewById(R.id.tvAdoptionNameDetail);
        adoptionTypeTv = findViewById(R.id.tvAdoptionTypeDetail);
        adoptionGenderTv = findViewById(R.id.tvAdoptionGenderDetail);
        adoptionRaceTv = findViewById(R.id.tvAdoptionRaceDetail);
        adoptionBirthTv = findViewById(R.id.tvAdoptionBirthDetail);
        adoptionColotTv = findViewById(R.id.tvAdoptionColorDetail);
        adoptionDescriptionTv = findViewById(R.id.tvAdoptionDescriptionDetail);
        backBtn = findViewById(R.id.btnBackAdoptionDetail);
        deleteBtn = findViewById(R.id.btnToDeleteMyAdoption);
        editBtn = findViewById(R.id.btnToEditMyAdoption);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAdoptionFromAdoptions = extras.getInt("idAdoptionFromAdoptions");
        }

        getAdoptionById(idAdoptionFromAdoptions);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(AdoptionDetailsActivity.this).create(); //Read Update
                alertDialog.setTitle("WARNING !!!");
                alertDialog.setMessage("do you want to delete your pet ?");

                alertDialog.setButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAdoptionById(idAdoptionFromAdoptions);
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void getAdoptionById(Integer idAdoptionFromAdoptions) {
        JsonObject object = new JsonObject();
        object.addProperty("id", idAdoptionFromAdoptions);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        ApiUtil.getServiceClass().getAdoptionById(object).enqueue(new Callback<Adoption>() {
            @Override
            public void onResponse(Call<Adoption> call, Response<Adoption> response) {
                final Adoption adoption = response.body();
                adoptionNameTv.setText(adoption.getName());
                adoptionTypeTv.setText(adoption.getType());
                adoptionGenderTv.setText(adoption.getGender());
                adoptionRaceTv.setText(adoption.getRace());
                adoptionBirthTv.setText(adoption.getBirthDate());
                adoptionColotTv.setText(adoption.getColor());
                adoptionDescriptionTv.setText(adoption.getDescription());
                if (adoption.getIdUser().equals(currentUser.getId())){
                    deleteBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Adoption> call, Throwable t) {
                System.out.println("error :"+ t.getMessage());
            }
        });
    }

    private void deleteAdoptionById(Integer id_pet) {
        JsonObject object = new JsonObject();
        object.addProperty("id", id_pet);

        ApiUtil.getServiceClass().deleteMyAdoptionById(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(AdoptionDetailsActivity.this, "adoption Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
