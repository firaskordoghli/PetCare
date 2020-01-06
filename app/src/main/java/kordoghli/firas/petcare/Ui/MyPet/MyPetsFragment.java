package kordoghli.firas.petcare.Ui.MyPet;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.MyPetsAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPetsFragment extends Fragment {

    private SessionManager sessionManager;
    private RecyclerView mRecycleView;
    private MyPetsAdapter petAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private TextView noPetsTv;
    FloatingActionButton toAddPetFab;


    public MyPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pets, container, false);

        noPetsTv = view.findViewById(R.id.tvNoPets);
        toAddPetFab = view.findViewById(R.id.fabToAddMyPet);
        mRecycleView = view.findViewById(R.id.rvMyPets);
        mRecycleView.setHasFixedSize(true);

        toAddPetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMyPetActivity.class);
                startActivity(intent);
            }
        });
        mShimmerViewContainer = view.findViewById(R.id.upCommingEventShimmer);

        // User Session Manager
        sessionManager = new SessionManager(getContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);
        listPets(currentUser.getId());
        return view;
    }

    private void listPets(Integer id_user) {
        JsonObject object = new JsonObject();
        object.addProperty("id_user", id_user);

        ApiUtil.getServiceClass().getAllMyPets(object).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                final List<Pet> petList = response.body();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (petList.size()==0){
                    noPetsTv.setVisibility(View.VISIBLE);
                }else {
                    mLayoutManager = new LinearLayoutManager(getContext());
                    petAdapter = new MyPetsAdapter(petList);
                    mRecycleView.setLayoutManager(mLayoutManager);
                    mRecycleView.setAdapter(petAdapter);

                    petAdapter.setOnItemClickListener(new MyPetsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(getContext(), petList.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MyPetDetailsActivity.class);
                            intent.putExtra("idPetFromMyPets", petList.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);
        listPets(currentUser.getId());
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

}
