package kordoghli.firas.petcare.Ui.Adoptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.squareup.picasso.Picasso;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.Blog.BlogDetailActivity;
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
    private MapView mapView;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_adoption_details);
        displayLoader();

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


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);



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
                new AlertDialog.Builder(AdoptionDetailsActivity.this)
                        .setTitle("Delete pet")
                        .setMessage("Are you sure you want to delete this pet?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAdoptionById(idAdoptionFromAdoptions);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
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

                Picasso.get().load(ApiUtil.photoUrl() +adoption.getPhoto()).into(adoptionIv);

                if (adoption.getIdUser().equals(currentUser.getId())){
                    deleteBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                }

                pDialog.dismiss();

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                    @Override
                                    public boolean onMapClick(@NonNull LatLng point) {
                                        Uri gmmIntentUri = Uri.parse("geo:" + adoption.getLatitude() + "," + adoption.getLongitude() + "?q=" + adoption.getLatitude() + "," + adoption.getLongitude());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(mapIntent);
                                        }
                                        return true;
                                    }
                                });
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(adoption.getLatitude(), adoption.getLongitude()))
                                        .zoom(10)
                                        .tilt(20)
                                        .build();
                                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(adoption.getLatitude(), adoption.getLongitude())));
                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);

                            }
                        });
                    }
                });
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

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AdoptionDetailsActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
