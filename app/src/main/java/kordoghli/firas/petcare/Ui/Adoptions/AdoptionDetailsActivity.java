package kordoghli.firas.petcare.Ui.Adoptions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdoptionDetailsActivity extends AppCompatActivity {
    private ImageView adoptionIv;
    private TextView adoptionNameTv,adoptionTypeTv,adoptionGenderTv,adoptionRaceTv
            ,adoptionBirthTv,adoptionColotTv,adoptionDescriptionTv;
    private Integer idAdoptionFromAdoptions = null;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAdoptionFromAdoptions = extras.getInt("idAdoptionFromAdoptions");
        }

        getAdoptionById(idAdoptionFromAdoptions);
    }

    private void getAdoptionById(Integer idAdoptionFromAdoptions) {
        JsonObject object = new JsonObject();
        object.addProperty("id", idAdoptionFromAdoptions);
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
            }

            @Override
            public void onFailure(Call<Adoption> call, Throwable t) {
                System.out.println("error :"+ t.getMessage());
            }
        });
    }
}
