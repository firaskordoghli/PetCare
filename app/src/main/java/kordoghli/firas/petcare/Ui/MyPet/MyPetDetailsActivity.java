package kordoghli.firas.petcare.Ui.MyPet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPetDetailsActivity extends AppCompatActivity {

    private ImageView petIv,petGenderIv;
    private TextView petNameTv,petBirthTv,petTypeTv,petRaceTv,petColorTv,petDescriptionTv;
    private Integer idPetFromMyPets = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet_details);

        petIv = findViewById(R.id.ivPetDetail);
        petGenderIv = findViewById(R.id.ivPetGenderDetail);
        petNameTv = findViewById(R.id.tvPetNameDetails);
        petBirthTv = findViewById(R.id.tvPetBirthDetail);
        petTypeTv = findViewById(R.id.tvPetTypeDetail);
        petRaceTv = findViewById(R.id.tvPetRaceDetail);
        petColorTv = findViewById(R.id.tvPetColorDetail);
        petDescriptionTv = findViewById(R.id.tvPetDEscriptionDetail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPetFromMyPets = extras.getInt("idPetFromMyPets");
        }

        getPetById(idPetFromMyPets);
    }

    private void getPetById(Integer id_pet){
        JsonObject object = new JsonObject();
        object.addProperty("id", id_pet);

        ApiUtil.getServiceClass().getMyPetById(object).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                final Pet myPet = response.body().get(0);
                petNameTv.setText(myPet.getName());
                petBirthTv.setText(myPet.getBirthDate());
                petTypeTv.setText(myPet.getType());
                petRaceTv.setText(myPet.getRace());
                petColorTv.setText(myPet.getColor());
                petDescriptionTv.setText(myPet.getDescription());
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Toast.makeText(MyPetDetailsActivity.this,"error "+ t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("error :"+ t.getMessage());
            }
        });
    }
}
