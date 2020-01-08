package kordoghli.firas.petcare.Ui.MyPet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.Data.Program;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPetDetailsActivity extends AppCompatActivity {

    private ImageView petIv, petGenderIv;
    private TextView petNameTv, petBirthTv, petTypeTv, petRaceTv, petColorTv, petDescriptionTv, showTv, noProgramTv;
    private ImageButton backBtn, deleteBtn, editBtn, toAddProgramBtn;
    private Integer idPetFromMyPets = null;
    private LinearLayout btnExpand, expandableView;
    private Boolean expanded = false;
    private CardView cardNails, cardMedicament, cardCoat, cardVaccine, cardShower;
    private TextView dateNails, dateMedicament, dateCoat, dateVaccine, dateShower;

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
        backBtn = findViewById(R.id.btnBackMyPetDetails);
        deleteBtn = findViewById(R.id.btnDeleteMyPet);
        editBtn = findViewById(R.id.btnToEditMyPet);
        toAddProgramBtn = findViewById(R.id.btnToAddProgram);
        btnExpand = findViewById(R.id.btnExpand);
        expandableView = findViewById(R.id.expandableView);
        showTv = findViewById(R.id.tvShow);
        noProgramTv = findViewById(R.id.tvNoProgram);

        cardNails = findViewById(R.id.cardNail);
        cardMedicament = findViewById(R.id.cardPills);
        cardCoat = findViewById(R.id.cardCoat);
        cardVaccine = findViewById(R.id.cardVaccine);
        cardShower = findViewById(R.id.cardShower);

        dateCoat = findViewById(R.id.tvDateCoat);
        dateMedicament = findViewById(R.id.tvDateMedication);
        dateNails = findViewById(R.id.dateNail);
        dateShower = findViewById(R.id.dateShower);
        dateVaccine = findViewById(R.id.dateVaccine);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPetFromMyPets = extras.getInt("idPetFromMyPets");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MyPetDetailsActivity.this)
                        .setTitle("Delete post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletePetById(idPetFromMyPets);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateMyPetActivity.class);
                intent.putExtra("idPetFromDetails", idPetFromMyPets);
                startActivity(intent);
            }
        });

        getPetById(idPetFromMyPets);
        getProgramForPet(idPetFromMyPets);

        if (!expanded) {
            expandableView.setVisibility(LinearLayout.GONE);
            showTv.setText("show");
        } else if (expanded) {
            expandableView.setVisibility(LinearLayout.VISIBLE);
            showTv.setText("hide");
        }

        toAddProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(idPetFromMyPets);
            }
        });


        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded) {
                    expanded = false;
                    expandableView.setVisibility(LinearLayout.GONE);
                    showTv.setText("show");
                } else if (!expanded) {
                    expanded = true;
                    expandableView.setVisibility(LinearLayout.VISIBLE);
                    showTv.setText("hide");
                }
            }
        });
    }

    private void getProgramForPet(Integer id_pet) {
        JsonObject object = new JsonObject();
        object.addProperty("id_pet", id_pet);

        ApiUtil.getServiceClass().getProgramForPet(object).enqueue(new Callback<List<Program>>() {
            @Override
            public void onResponse(Call<List<Program>> call, Response<List<Program>> response) {
                List<Program> programList = response.body();
                if (programList.size() == 0) {
                    noProgramTv.setVisibility(View.VISIBLE);
                } else {
                    noProgramTv.setVisibility(View.GONE);
                    for (int i = 0; i < programList.size(); i++) {
                        if (programList.get(i).getType().equals("nails")){
                            cardNails.setVisibility(View.VISIBLE);
                            dateNails.setText(programList.get(i).getDate());
                        }
                        if (programList.get(i).getType().equals("medication")){
                            cardMedicament.setVisibility(View.VISIBLE);
                            dateMedicament.setText(programList.get(i).getDate());
                        }
                        if (programList.get(i).getType().equals("coat")){
                            cardCoat.setVisibility(View.VISIBLE);
                            dateCoat.setText(programList.get(i).getDate());
                        }
                        if (programList.get(i).getType().equals("shower")){
                            cardShower.setVisibility(View.VISIBLE);
                            dateShower.setText(programList.get(i).getDate());
                        }
                        if (programList.get(i).getType().equals("vaccine")){
                            cardVaccine.setVisibility(View.VISIBLE);
                            dateVaccine.setText(programList.get(i).getDate());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Program>> call, Throwable t) {

            }
        });
    }

    private void getPetById(Integer id_pet) {
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
                Picasso.get().load(ApiUtil.photoUrl() + myPet.getPhoto()).into(petIv);
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Toast.makeText(MyPetDetailsActivity.this, "error " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("error :" + t.getMessage());
            }
        });
    }

    private void deletePetById(Integer id_pet) {
        JsonObject object = new JsonObject();
        object.addProperty("id", id_pet);

        ApiUtil.getServiceClass().deleteMyPetById(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(MyPetDetailsActivity.this, "pet Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void openDialog(Integer id_pet) {
        ProgramDialog programDialog = new ProgramDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("id_pet", id_pet);
        programDialog.setArguments(bundle);
        programDialog.show(getSupportFragmentManager(), "program dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetById(idPetFromMyPets);
        getProgramForPet(idPetFromMyPets);
    }
}
