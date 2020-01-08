package kordoghli.firas.petcare.Ui.LostAndFound;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import kordoghli.firas.petcare.Data.Lost;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostDetailActivity extends AppCompatActivity {
    private ImageView lostDetailIv,callLostDetailIv;
    private TextView genderTv, typeTv, descriptionTv, userNameTv,classificationTv;
    private MapView mapView;
    private Integer idLostFromLostAndFound = null;
    private SessionManager sessionManager;
    private Integer phoneNumber = null ;
    private ConstraintLayout foundByCl;
    private FloatingActionButton deleteLostFab;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_lost_detail);
        displayLoader();

        lostDetailIv = findViewById(R.id.ivLostDetail);
        genderTv = findViewById(R.id.tvLostDetailGender);
        typeTv = findViewById(R.id.tvLostDetailType);
        descriptionTv = findViewById(R.id.tvLostDetailDescription);
        userNameTv = findViewById(R.id.tvLostDetailUser);
        callLostDetailIv = findViewById(R.id.ivLostDetailCall);
        classificationTv = findViewById(R.id.tvLostDetailClassification);
        foundByCl = findViewById(R.id.clFoundBy);
        deleteLostFab = findViewById(R.id.fadLostDetailDelete);

        mapView = (MapView) findViewById(R.id.mapViewLostDetail);
        mapView.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idLostFromLostAndFound = extras.getInt("idLostFromLostAndFound");
        }
        getLostbyId(idLostFromLostAndFound);

        callLostDetailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
            }
        });

        deleteLostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LostDetailActivity.this)
                        .setTitle("Delete post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteLost(idLostFromLostAndFound);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            }
        });

    }

    private void  deleteLost (Integer id){
        JsonObject object = new JsonObject();
        object.addProperty("id", id);

        ApiUtil.getServiceClass().deleteLostById(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(LostDetailActivity.this, "post Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getUserById(Integer id){
        JsonObject object = new JsonObject();
        object.addProperty("id", id);

        ApiUtil.getServiceClass().getUserById(object).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user =response.body();
                phoneNumber = user.getPhone();
                userNameTv.setText("Found by " + user.getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void getLostbyId(Integer id) {
        JsonObject object = new JsonObject();
        object.addProperty("id", idLostFromLostAndFound);
        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        ApiUtil.getServiceClass().getLostById(object).enqueue(new Callback<Lost>() {
            @Override
            public void onResponse(Call<Lost> call, Response<Lost> response) {
                final Lost lost = response.body();
                genderTv.setText(lost.getGender());
                typeTv.setText(lost.getType());
                descriptionTv.setText(lost.getDescription());
                classificationTv.setText(lost.getClassification());
                Picasso.get().load(ApiUtil.photoUrl() +lost.getPhoto()).into(lostDetailIv);
                if (currentUser.getId() == lost.getIdUser()){
                    foundByCl.setVisibility(View.GONE);
                    deleteLostFab.show();
                }else {
                    deleteLostFab.hide();
                    foundByCl.setVisibility(View.VISIBLE);
                }

                if (lost.getClassification().equals("Lost")){
                    classificationTv.setBackgroundColor(Color.parseColor("#d11141"));
                }else if (lost.getClassification().equals("Found")) {
                    classificationTv.setBackgroundColor(Color.parseColor("#00aedb"));
                }

                getUserById(lost.getIdUser());

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
                                        Uri gmmIntentUri = Uri.parse("geo:" + lost.getLatitude() + "," + lost.getLongitude() + "?q=" + lost.getLatitude() + "," + lost.getLongitude());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(mapIntent);
                                        }
                                        return true;
                                    }
                                });
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(lost.getLatitude(), lost.getLongitude()))
                                        .zoom(10)
                                        .tilt(20)
                                        .build();
                                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lost.getLatitude(), lost.getLongitude())));
                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Lost> call, Throwable t) {
                pDialog.dismiss();
                System.out.println("error :" + t.getMessage());
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
        pDialog = new ProgressDialog(LostDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
